package com.rassame.trader.client.paxful.protocol;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OfferType {
    SELL("sell"),
    BUY("buy");

    @JsonValue
    private final String value;

    OfferType(String value) {
        this.value = value;
    }
}
