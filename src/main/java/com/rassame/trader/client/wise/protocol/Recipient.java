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
public class Recipient {
    private Long id;
    private Integer profile;
    private String accountHolderName;
    private String currency;
    private String type;
    private Map<String, Object> details;
}
