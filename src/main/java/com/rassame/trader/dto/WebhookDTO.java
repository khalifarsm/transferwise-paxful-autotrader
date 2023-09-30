package com.rassame.trader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class WebhookDTO {
    private Long time;
    private String type;
    private TradeDTO payload;
}
