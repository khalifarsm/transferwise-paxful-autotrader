package com.rassame.trader.client.paxful;

import com.rassame.trader.client.paxful.protocol.GetTradeResponse;
import com.rassame.trader.client.paxful.protocol.OfferType;
import com.rassame.trader.client.paxful.protocol.StatusResponse;
import com.rassame.trader.client.paxful.protocol.TradeStatus;
import com.rassame.trader.config.UnirestConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TradeClient extends BasePaxfulClient {
    public TradeClient(UnirestConfig unirestConfig) {
        super(unirestConfig);
    }

//    @PostConstruct
//    public void setup() throws Exception {
//        System.out.println(getTrade("y1RNpTrBSMi"));
//    }

    public GetTradeResponse getTrade(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("trade_hash", id);
        GetTradeResponse getTradeResponse = (GetTradeResponse) post(map, "/trade/get", GetTradeResponse.class);

        //for test
//        getTradeResponse.getData().getTrade().setTradeStatus(TradeStatus.ACTIVE_FUNDED);
//        getTradeResponse.getData().getTrade().setOfferType(OfferType.BUY);
//        getTradeResponse.getData().getTrade().setFiatCurrencyCode("EUR");
        return getTradeResponse;
    }

    public StatusResponse releaseCryptoForTrade(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("trade_hash", id);
        return (StatusResponse) post(map, "/trade/release", StatusResponse.class);
    }

    public StatusResponse markTradePaid(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("trade_hash", id);
        return (StatusResponse) post(map, "/trade/paid", StatusResponse.class);
    }
}
