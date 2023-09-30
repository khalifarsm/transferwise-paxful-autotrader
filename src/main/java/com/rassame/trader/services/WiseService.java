package com.rassame.trader.services;


import com.rassame.trader.client.currency.CurrencyClient;
import com.rassame.trader.client.paxful.TradeChatClient;
import com.rassame.trader.client.paxful.TradeClient;
import com.rassame.trader.client.paxful.TradeFeedbackClient;
import com.rassame.trader.client.paxful.protocol.GetTradeResponse;
import com.rassame.trader.client.paxful.protocol.OfferType;
import com.rassame.trader.client.paxful.protocol.Trade;
import com.rassame.trader.client.paxful.protocol.TradeStatus;
import com.rassame.trader.client.wise.QuoteClient;
import com.rassame.trader.client.wise.RecipientClient;
import com.rassame.trader.client.wise.StatementClient;
import com.rassame.trader.client.wise.TransferClient;
import com.rassame.trader.client.wise.protocol.*;
import com.rassame.trader.domain.Call;
import com.rassame.trader.exceptions.InvalidInputException;
import com.rassame.trader.exceptions.InvalidOperationException;
import com.rassame.trader.exceptions.NotFoundException;
import com.rassame.trader.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class WiseService {

    private final TradeClient tradeClient;
    private final StatementClient statementClient;
    private final TransactionRepository transactionRepository;
    private final QuoteClient quoteClient;
    private final RecipientClient recipientClient;
    private final TransferClient transferClient;
    private final CallService callService;
    private final TradeChatClient tradeChatClient;
    private final TradeFeedbackClient tradeFeedbackClient;
    private final MultiWiseService multiWiseService;
    private final OfferUpdateService offerUpdateService;

    private final CurrencyClient currencyClient;
    @Value("${api.wise.search.duration}")
    private Integer searchDuration;

//    @PostConstruct
//    public void setup() {
//        transfer("9600000799589408", "084009519", "ilyas lamnouni", 20.0, "USD", new Call());
//    }

    public Transfer transfer(String email, String holder, Double amount, String currency, Call call) {
        Quote quote = quoteClient.createQuote(currency, amount);
        if (quote == null || quote.getId() == null) {
            callService.error(call, "failed to create transfer");
            throw new InvalidOperationException();
        }
        Recipient recipient = recipientClient.createRecipient(email, holder, currency);
        if (recipient == null || recipient.getId() == null) {
            callService.error(call, "failed to create recipient account");
            throw new InvalidOperationException();
        }
        Transfer transfer = transferClient.createTransfer(recipient.getId(), quote.getId());
        if (transfer == null || transfer.getId() == null) {
            callService.error(call, "failed to create transfer");
            throw new InvalidOperationException();
        }
        FundTransferResponse response = transferClient.fundTransfer(transfer.getId());
        if (response.getStatus().equalsIgnoreCase("COMPLETED")) {
            return transfer;
        } else {
            callService.error(call, "transfer ID : " + transfer.getId() + " cannot be sent");
            throw new InvalidOperationException();
        }
    }

    public Transfer transfer(String accountNumber, String routingNumber, String holder, Double amount, String currency, Call call) {
        Quote quote = quoteClient.createQuote(currency, amount);
        if (quote == null || quote.getId() == null) {
            callService.error(call, "failed to create transfer");
            throw new InvalidOperationException();
        }
        Recipient recipient = recipientClient.createRecipient(accountNumber, routingNumber, holder, currency);
        if (recipient == null || recipient.getId() == null) {
            callService.error(call, "failed to create recipient account");
            throw new InvalidOperationException();
        }
        Transfer transfer = transferClient.createTransfer(recipient.getId(), quote.getId());
        if (transfer == null || transfer.getId() == null) {
            callService.error(call, "failed to create transfer");
            throw new InvalidOperationException();
        }
        FundTransferResponse response = transferClient.fundTransfer(transfer.getId());
        if (response.getStatus().equalsIgnoreCase("COMPLETED")) {
            return transfer;
        } else {
            callService.error(call, "transfer ID : " + transfer.getId() + " cannot be sent");
            throw new InvalidOperationException();
        }
    }

    public void sellUSDT(String tradeId, String email) {
        Call call = callService.createCall(email, tradeId, null, com.rassame.trader.domain.TransactionType.SELL);

        validateTradeId(tradeId, call);
        GetTradeResponse getTradeResponse = tradeClient.getTrade(tradeId);
        validateTrade(tradeId, getTradeResponse, OfferType.BUY, TradeStatus.ACTIVE_FUNDED, call);
        Trade trade = getTradeResponse.getData().getTrade();

        Transfer transfer = transfer(email, trade.getSellerFullName().toString(), Double.valueOf(trade.getFiatAmountRequested()), trade.getFiatCurrencyCode().toUpperCase(), call);
        if (transfer != null) {
            tradeClient.markTradePaid(tradeId);
            transactionRepository.save(new com.rassame.trader.domain.Transaction()
                    .setAccount(multiWiseService.getWise().getId())
                    .setCreated(LocalDateTime.now())
                    .setTransaction(transfer.toString())
                    .setTrade(trade.toString())
                    .setType(com.rassame.trader.domain.TransactionType.SELL)
                    .setCurrency(trade.getFiatCurrencyCode().toUpperCase())
                    .setReceived(Double.valueOf(trade.getCryptoAmountRequested()) / 1000000)
                    .setSent(getFiatValue(trade)));
            callService.success(call);
            giveFeedback(trade.getTradeHash());
            new Thread(() -> {
                offerUpdateService.updateSell();
            });
        }
    }

    public void sellUSDT(String tradeId, String accountNumber, String routingNumber) {
        Call call = callService.createCall(accountNumber, tradeId, null, com.rassame.trader.domain.TransactionType.SELL);

        validateTradeId(tradeId, call);
        GetTradeResponse getTradeResponse = tradeClient.getTrade(tradeId);
        validateTrade(tradeId, getTradeResponse, OfferType.BUY, TradeStatus.ACTIVE_FUNDED, call);
        Trade trade = getTradeResponse.getData().getTrade();

        Transfer transfer = transfer(accountNumber, routingNumber, trade.getSellerFullName().toString(), Double.valueOf(trade.getFiatAmountRequested()), trade.getFiatCurrencyCode().toUpperCase(), call);
        if (transfer != null) {
            tradeClient.markTradePaid(tradeId);
            transactionRepository.save(new com.rassame.trader.domain.Transaction()
                    .setAccount(multiWiseService.getWise().getId())
                    .setCreated(LocalDateTime.now())
                    .setTransaction(transfer.toString())
                    .setTrade(trade.toString())
                    .setCurrency(trade.getFiatCurrencyCode().toUpperCase())
                    .setType(com.rassame.trader.domain.TransactionType.SELL)
                    .setReceived(Double.valueOf(trade.getCryptoAmountRequested()) / 1000000)
                    .setSent(getFiatValue(trade)));
            callService.success(call);
            giveFeedback(trade.getTradeHash());
            new Thread(() -> {
                offerUpdateService.updateSell();
            });
        }
    }

    public void buyUSDTWithBuyerName(GetTradeResponse getTradeResponse) {
        if (getTradeResponse.getData().getTrade().getBuyerFullName().toString().length() <= 3) {
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(searchDuration);
        Statement statement = statementClient.getStatement(getTradeResponse.getData().getTrade().getFiatCurrencyCode().toUpperCase(), start.format(formatter), now.format(formatter));
        if (statement == null || statement.getTransactions() == null) {
            return;
        }
        Transaction transaction = getTransactionFromStatement(statement, getTradeResponse.getData().getTrade().getBuyerFullName().toString());
        if (transaction == null) return;
        try {
            buyUSDT(getTradeResponse.getData().getTrade().getTradeHash(), transaction.getReferenceNumber());
        } catch (Exception ex) {
            log.info(ex.toString());
        }
    }

    public void buyUSDT(String tradeId, String transferId) {
        Call call = callService.createCall(null, tradeId, transferId, com.rassame.trader.domain.TransactionType.BUY);

        transferId = validateTransfer(transferId, call);
        validateTradeId(tradeId, call);
        GetTradeResponse getTradeResponse = tradeClient.getTrade(tradeId);
        validateTrade(tradeId, getTradeResponse, OfferType.SELL, TradeStatus.PAID, call);
        Trade trade = getTradeResponse.getData().getTrade();

        //get Transaction
        Transaction transaction = getTransaction(getTradeResponse, transferId, call);

        checkMatch(trade, transaction, call);

        tradeClient.releaseCryptoForTrade(tradeId);
        transactionRepository.save(new com.rassame.trader.domain.Transaction()
                .setAccount(multiWiseService.getWise().getId())
                .setCreated(LocalDateTime.now())
                .setTrade(trade.toString())
                .setTransaction(transaction.toString())
                .setReceived(getFiatValue(trade))
                .setCurrency(trade.getFiatCurrencyCode().toUpperCase())
                .setSent(Double.valueOf(trade.getCryptoAmountRequested()) / 1000000)
                .setType(com.rassame.trader.domain.TransactionType.BUY));
        callService.success(call);
        giveFeedback(trade.getTradeHash());
    }

    private Transaction getTransaction(GetTradeResponse getTradeResponse, String transferId, Call call) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(searchDuration);
        try {
            Statement statement = statementClient.getStatement(getTradeResponse.getData().getTrade().getFiatCurrencyCode().toUpperCase(), start.format(formatter), now.format(formatter));
            if (statement == null || statement.getTransactions() == null) {
                callService.error(call, "failed to get statement from current wise");
                throw new InvalidOperationException();
            }
            Transaction transaction = getTransactionFromStatement(statement, transferId, call);
            return transaction;
        } catch (NotFoundException ex) {
            log.info("transfer {} not found on wise account {}", transferId, multiWiseService.getWise().getId());
            for (Wise wise : multiWiseService.getOthers()) {
                try {
                    Statement statement = statementClient.getStatement(getTradeResponse.getData().getTrade().getFiatCurrencyCode().toUpperCase(), start.format(formatter), now.format(formatter), wise);
                    if (statement == null || statement.getTransactions() == null) {
                        log.info("failed to get statement from wise " + wise.getId());
                        continue;
                    }
                    Transaction transaction = getTransactionFromStatement(statement, transferId, call);
                    return transaction;
                } catch (NotFoundException exception) {
                    log.info("transfer {} not found on wise account {}", transferId, wise.getId());
                }
            }
        }
        callService.error(call, "wise transaction (transfer) not found ID : " + transferId);
        throw new NotFoundException();
    }

    private void giveFeedback(final String tradeId) {
        new Thread(() -> {
            tradeChatClient.createChatMessage(tradeId, "thank you");
            tradeFeedbackClient.createFeedback(tradeId, "good experience", 1);
            tradeChatClient.createChatMessage(tradeId, "please do not forget to leave a feedback");
        }).start();
    }

    private void checkMatch(Trade trade, Transaction transaction, Call call) {
        //check currency is the same
        if (!transaction.getAmount().getCurrency().equalsIgnoreCase(trade.getFiatCurrencyCode())) {
            callService.error(call, "currencies does not match");
            throw new InvalidOperationException();
        }
        //check amount
        Double tradeAmount = Double.valueOf(trade.getFiatAmountRequested());
        Double transactionAmount = Double.valueOf(transaction.getAmount().getValue());
        if (transactionAmount < tradeAmount) {
            callService.error(call, "transaction amount is smaller than trade amount " + tradeAmount + "/" + tradeAmount);
            throw new InvalidOperationException();
        }
    }

    private void validateTradeId(String id, Call call) {
        if (id == null) {
            callService.error(call, "trade ID is NULL");
            throw new InvalidInputException();
        }
        if (id.length() > 12 || id.length() < 10) {
            log.info("trade ID is not valid");
            throw new InvalidInputException();
        }
    }

    private String validateTransfer(String id, Call call) {
        if (id == null) {
            callService.error(call, "transaction ID is NULL");
            throw new InvalidInputException();
        }
        id = id.replace("#", "");
        id = id.replace("TRANSFER", "");
        id = id.replace("-", "");
        if (id.length() > 10 || id.length() < 6) {
            callService.error(call, "transaction ID is not valid (size) : " + id);
            throw new InvalidInputException();
        }
        for (char c : id.toCharArray()) {
            if (!Character.isDigit(c)) {
                callService.error(call, "transaction ID not valid (non digit character) : " + id);
                throw new InvalidInputException();
            }
        }
        return id;
    }

    public void validateTrade(String tradeId, GetTradeResponse getTradeResponse, OfferType offerType, TradeStatus tradeStatus, Call call) {
        if (getTradeResponse.getData() == null) {
            callService.error(call, "trade " + tradeId + " not found");
            throw new NotFoundException();
        }
        Trade trade = getTradeResponse.getData().getTrade();
        if (trade.getOfferType() != offerType) {
            callService.error(call, "trade " + tradeId + " is not " + offerType + " trade");
            throw new InvalidInputException();
        }
        if (trade.getTradeStatus() != tradeStatus) {
            callService.error(call, "trade " + tradeId + " is not " + tradeStatus);
            throw new InvalidInputException();
        }
    }

    private Transaction getTransactionFromStatement(Statement statement, String transactionId, Call call) {
        if (statement.getTransactions() == null) {
            throw new NotFoundException();
        }
        for (Transaction transaction : statement.getTransactions()) {
            if (transaction.getType() == TransactionType.CREDIT && transaction.getReferenceNumber().startsWith("TRANSFER") && transaction.getReferenceNumber().endsWith(transactionId)) {
                return transaction;
            }
        }
        callService.error(call, "wise transaction (transfer) not found ID : " + transactionId);
        throw new NotFoundException();
    }

    private Transaction getTransactionFromStatement(Statement statement, String name) {
        if (statement.getTransactions() == null) {
            return null;
        }
        for (Transaction transaction : statement.getTransactions()) {
            if (transaction.getType() == TransactionType.CREDIT && transaction.getReferenceNumber().startsWith("TRANSFER") && transaction.getDetails().getSenderName().toUpperCase().startsWith(name.toUpperCase())) {
                log.info("found transaction with the same sender name sender={} trader={}", transaction.getDetails().getSenderName(), name);
                return transaction;
            }
        }
        return null;
    }

    public Double getFiatValue(Trade trade) {
        Double fiat = Double.valueOf(trade.getFiatAmountRequested());
        if (!trade.getFiatCurrencyCode().equalsIgnoreCase("USD")) {
            fiat = fiat * currencyClient.get(trade.getFiatCurrencyCode().toUpperCase());
        }
        return fiat;
    }

}
