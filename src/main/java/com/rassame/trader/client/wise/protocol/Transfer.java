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
public class Transfer {
    private Long id;
    private Long targetAccount;
    private Long sourceAccount;
    private String quoteUuid;
    private String customerTransactionId;
    private Map<String, String> details;
}
