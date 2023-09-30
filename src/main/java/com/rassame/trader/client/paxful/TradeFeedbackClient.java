package com.rassame.trader.client.paxful;

import com.rassame.trader.client.paxful.protocol.StatusResponse;
import com.rassame.trader.config.UnirestConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class TradeFeedbackClient extends BasePaxfulClient {
    public TradeFeedbackClient(UnirestConfig unirestConfig) {
        super(unirestConfig);
    }

//    @PostConstruct
//    public void setup() throws Exception {
//        System.out.println(createFeedback("E5Ke5oCFHUE","great%20experience",1));
//    }

    public StatusResponse createFeedback(String id, String message, Integer rating) {
        Map<String, Object> map = new HashMap<>();
        map.put("trade_hash", id);
        map.put("rating", rating);
        map.put("message", message.replace(" ","%20"));
        return (StatusResponse) post(map, "/feedback/give", StatusResponse.class);
    }
}
