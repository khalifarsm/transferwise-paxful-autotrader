package com.rassame.trader.client.paxful.protocol;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TradeStatus {
    NOT_FUNDED("Not funded"),
    FUNDS_PROCESSING("Funds processing"),
    FUNDS_PROCESSED("Funds processed"),
    ACTIVE_FUNDED("Active funded"),
    PAID("Paid"),
    CANCELED_SYSTEM("Cancelled system"),
    CANCELED_BUYER("Cancelled buyer"),
    CANCELED_SELLER("Cancelled seller"),
    RELEASED("Released"),
    DISPUTE_OPEN("Dispute open"),
    DISPUTE_WINS_SELLER("Dispute wins seller"),
    DISPUTE_WINS_BUYER("Dispute wins buyer");

    @JsonValue
    private final String value;

    TradeStatus(String value) {
        this.value = value;
    }
}
