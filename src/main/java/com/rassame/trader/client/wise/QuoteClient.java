package com.rassame.trader.client.wise;

import com.rassame.trader.client.wise.protocol.CreateQuoteRequest;
import com.rassame.trader.client.wise.protocol.Quote;
import com.rassame.trader.config.UnirestConfig;
import com.rassame.trader.services.MultiWiseService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class QuoteClient extends BaseWiseClient {

    public QuoteClient(UnirestConfig unirestConfig, MultiWiseService multiWiseService) {
        super(multiWiseService, unirestConfig);
    }

//    @PostConstruct
//    public void setup() {
//        System.out.println(createQuote("USD", 10.0));
//    }

    public Quote createQuote(String currency, Double amount) {
        CreateQuoteRequest createQuoteRequest = new CreateQuoteRequest()
                .setProfile(Integer.valueOf(multiWiseService.getWise().getProfileId()))
                .setSourceCurrency(currency)
                .setTargetCurrency(currency)
                .setTargetAmount(amount)
                .setPayIn("BALANCE")
                .setPayOut("BALANCE");
        Quote quote = (Quote) post(createQuoteRequest, "/v2/quotes", Quote.class);
        return quote;
    }
}
