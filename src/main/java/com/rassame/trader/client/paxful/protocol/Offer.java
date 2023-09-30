package com.rassame.trader.client.paxful.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Offer {
    @JsonProperty("offer_id")
    private String offerId;
    @JsonProperty("fiat_usd_price_per_crypto")
    private Double price;
    @JsonProperty("fiat_amount_range_max")
    private Integer max;
}
