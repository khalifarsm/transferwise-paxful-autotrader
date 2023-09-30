package com.rassame.trader.client.paxful.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Trade {
    @JsonProperty("trade_hash")
    private String tradeHash;

    @JsonProperty("buyer_external_id")
    private String buyerExternalId;
    @JsonProperty("buyer_name")
    private String buyerName;
    @JsonProperty("buyer_full_name")
    private Name buyerFullName;

    @JsonProperty("seller_external_id")
    private String sellerExternalId;
    @JsonProperty("seller_name")
    private String sellerName;
    @JsonProperty("seller_full_name")
    private Name sellerFullName;

    @JsonProperty("trade_status")
    private TradeStatus tradeStatus;
    @JsonProperty("offer_type")
    private OfferType offerType;

    @JsonProperty("fiat_amount_requested")
    private String fiatAmountRequested;
    @JsonProperty("crypto_amount_requested")
    private String cryptoAmountRequested;

    @JsonProperty("offer_hash")
    private String offerHash;

    @JsonProperty("fee_percentage")
    private String feePercentage;
    @JsonProperty("margin")
    private String margin;

    @JsonProperty("payment_method_name")
    private String paymentMethodName;
    @JsonProperty("crypto_currency_code")
    private String cryptoCurrencyCode;
    @JsonProperty("fiat_currency_code")
    private String fiatCurrencyCode;

    @JsonProperty("started_at")
    private String started_at;
}
