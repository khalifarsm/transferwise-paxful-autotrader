package com.rassame.trader.client.paxful.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class OfferData {
    private int limit;
    private List<Offer> offers;
}
