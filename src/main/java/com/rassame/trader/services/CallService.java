package com.rassame.trader.services;

import com.rassame.trader.domain.Call;
import com.rassame.trader.domain.TransactionStatus;
import com.rassame.trader.domain.TransactionType;
import com.rassame.trader.exceptions.DuplicateException;
import com.rassame.trader.repository.CallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallService {
    private final CallRepository callRepository;

    public Call createCall(String email, String tradeId, String transferId, TransactionType transactionType) {
        if (transactionType == TransactionType.BUY) {
            List<TransactionStatus> statuses = new ArrayList<>();
            statuses.add(TransactionStatus.SUCCEEDED);
            statuses.add(TransactionStatus.PENDING);
            List<Call> calls = callRepository.findByTransferIdAndStatusIn(transferId, statuses);
            if (calls.size() > 0) {
                log.info("another call with this transferId : {} has succeeded or in progress", transferId);
                callRepository.save(new Call()
                        .setEmail(email)
                        .setTradeId(tradeId)
                        .setTransferId(transferId)
                        .setType(transactionType)
                        .setStatus(TransactionStatus.FAILED)
                        .setError("another call with this transferId : " + transferId + " has succeeded or in progress"));
                throw new DuplicateException();
            }
        }
        if (transactionType == TransactionType.SELL) {
            List<TransactionStatus> statuses = new ArrayList<>();
            statuses.add(TransactionStatus.SUCCEEDED);
            statuses.add(TransactionStatus.PENDING);
            List<Call> calls = callRepository.findByTradeIdAndStatusIn(tradeId, statuses);
            if (calls.size() > 0) {
                log.info("another call with this trade : {} has succeeded or in progress", tradeId);
                callRepository.save(new Call()
                        .setEmail(email)
                        .setTradeId(tradeId)
                        .setTransferId(transferId)
                        .setType(transactionType)
                        .setStatus(TransactionStatus.FAILED)
                        .setError("another call with this tradeId : " + tradeId + " has succeeded or in progress"));
                throw new DuplicateException();
            }
        }
        Call call = callRepository.save(new Call()
                .setEmail(email)
                .setTradeId(tradeId)
                .setTransferId(transferId)
                .setType(transactionType)
                .setStatus(TransactionStatus.PENDING));
        log.info("call received : " + call.getId());
        return call;
    }

    public Call error(Call call, String error) {
        log.warn(error + " : " + call.getId());
        call.setError(error);
        call.setStatus(TransactionStatus.FAILED);
        return callRepository.save(call);
    }

    public Call success(Call call) {
        log.info("transaction completed with success : " + call.getId());
        call.setStatus(TransactionStatus.SUCCEEDED);
        return callRepository.save(call);
    }
}
