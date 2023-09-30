package com.rassame.trader.client.wise.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Accessors(chain = true)
public class FundTransferRequest {
    private String type = "BALANCE";
}
