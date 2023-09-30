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
public class CreateRecipientRequest {
    private Integer profile;
    private String accountHolderName="test";
    private String currency;
    private String type = "email";
    private Map<String, Object> details;
}
