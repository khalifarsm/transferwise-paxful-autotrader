package com.rassame.trader.client.wise;

import com.rassame.trader.client.wise.protocol.Amount;
import com.rassame.trader.client.wise.protocol.Statement;
import com.rassame.trader.client.wise.protocol.Transaction;
import com.rassame.trader.client.wise.protocol.TransactionType;
import com.rassame.trader.config.UnirestConfig;
import com.rassame.trader.services.MultiWiseService;
import com.rassame.trader.services.Wise;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatementClient extends BaseWiseClient {

    public StatementClient(MultiWiseService multiWiseService, UnirestConfig unirestConfig) {
        super(multiWiseService, unirestConfig);
    }

//    @PostConstruct
//    public void setup() throws Exception {
//        System.out.println(getStatement("EUR","2021-07-31T00:00:00.000Z","2021-08-15T23:59:59.999Z"));
//    }

    public Statement getStatement(String currency, String start, String end) {
        String params = "?currency=" + currency + "&intervalStart=" + start + "&intervalEnd=" + end + "&type=COMPACT";
        String path = "/v3/profiles/" + multiWiseService.getWise().getProfileId() + "/borderless-accounts/" + multiWiseService.getWise().getAccountId() + "/statement.json";
        Statement statement = (Statement) get(params, path, Statement.class);

        //for test
//        Transaction transaction = new Transaction();
//        Amount amount = new Amount();
//        amount.setCurrency("USD");
//        amount.setValue(18.00);
//        transaction.setAmount(amount);
//        transaction.setType(TransactionType.CREDIT);
//        transaction.setReferenceNumber("TRANSFER-12345678");
//        statement.getTransactions().add(transaction);
        return statement;
    }

    public Statement getStatement(String currency, String start, String end, Wise wise) {
        String params = "?currency=" + currency + "&intervalStart=" + start + "&intervalEnd=" + end + "&type=COMPACT";
        String path = "/v3/profiles/" + wise.getProfileId() + "/borderless-accounts/" + wise.getAccountId() + "/statement.json";
        Statement statement = (Statement) get(params, path, Statement.class, wise);
        return statement;
    }
}
