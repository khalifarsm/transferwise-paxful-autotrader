package com.rassame.trader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class TradeDTO {
    @JsonProperty("trade_id")
    private String tradeId;
    @JsonProperty("trade_hash")
    private String tradeHash;
}
