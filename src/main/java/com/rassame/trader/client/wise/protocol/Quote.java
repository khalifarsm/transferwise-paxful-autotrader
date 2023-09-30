package com.rassame.trader.client.wise.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Accessors(chain = true)
public class Quote {
    private String sourceCurrency;
    private String targetCurrency;
    private Double targetAmount;
    private Integer profile;
    private Double rate;
    private String id;
}
