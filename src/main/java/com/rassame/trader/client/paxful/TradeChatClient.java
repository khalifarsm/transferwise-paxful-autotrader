package com.rassame.trader.client.paxful;

import com.rassame.trader.client.paxful.protocol.StatusResponse;
import com.rassame.trader.config.UnirestConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class TradeChatClient extends BasePaxfulClient {
    public TradeChatClient(UnirestConfig unirestConfig) {
        super(unirestConfig);
    }

//    @PostConstruct
//    public void setup() throws Exception {
//        System.out.println(createChatMessage("E5Ke5oCFHUE","thank%20you"));
//    }

    public StatusResponse createChatMessage(String id, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("trade_hash", id);
        map.put("message", message.replace(" ","%20"));
        return (StatusResponse) post(map, "/trade-chat/post", StatusResponse.class);
    }
}
