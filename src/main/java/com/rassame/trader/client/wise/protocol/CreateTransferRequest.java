package com.rassame.trader.client.wise.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Accessors(chain = true)
public class CreateTransferRequest {
    private Long targetAccount;
    private String quoteUuid;
    private String customerTransactionId;
    private Map<String, String> details;
}
