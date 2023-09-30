package com.rassame.trader.client.paxful;

import com.rassame.trader.client.paxful.protocol.ListOffersResponse;
import com.rassame.trader.client.paxful.protocol.StatusResponse;
import com.rassame.trader.config.UnirestConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OfferClient extends BasePaxfulClient {
    public OfferClient(UnirestConfig unirestConfig) {
        super(unirestConfig);
    }

    public ListOffersResponse getOffers(String type, String currency) {
        Map<String, Object> map = new HashMap<>();
        map.put("offer_type", type);
        map.put("currency_code", currency);
        map.put("payment_method", "wise-transferwise");
        map.put("crypto_currency_code", "usdt");
        ListOffersResponse listOffersResponse = (ListOffersResponse) post(map, "/offer/all", ListOffersResponse.class);
        return listOffersResponse;
    }

    public StatusResponse changeOffersRate(String id, double margin) {
        Map<String, Object> map = new HashMap<>();
        map.put("offer_hash", id);
        map.put("margin", margin);
        StatusResponse statusResponse = (StatusResponse) post(map, "/offer/update", StatusResponse.class);
        return statusResponse;
    }

    public StatusResponse changeOffersRange(String id, double max) {
        Map<String, Object> map = new HashMap<>();
        map.put("offer_hash", id);
        map.put("range_max", max);
        StatusResponse statusResponse = (StatusResponse) post(map, "/offer/update", StatusResponse.class);
        return statusResponse;
    }

    public StatusResponse active(String id, boolean active) {
        if (active) {
            return activate(id);
        } else {
            return deactivate(id);
        }
    }

    public StatusResponse activate(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("offer_hash", id);
        StatusResponse statusResponse = (StatusResponse) post(map, "/offer/activate", StatusResponse.class);
        return statusResponse;
    }

    public StatusResponse deactivate(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("offer_hash", id);
        StatusResponse statusResponse = (StatusResponse) post(map, "/offer/deactivate", StatusResponse.class);
        return statusResponse;
    }
}
