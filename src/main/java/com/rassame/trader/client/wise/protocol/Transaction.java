package com.rassame.trader.client.wise.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Transaction {
    private String referenceNumber;
    private TransactionType type;
    private Amount amount;
    private Amount runningBalance;
    private Details details;
}
