package com.rassame.trader.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AnalyticsDTO {
    private Double received;
    private Double sent;

    public Double getEarned() {
        return received - sent;
    }
}
