package com.rassame.trader.client.wise.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Details {
    private String type;
    private String senderName;
}
