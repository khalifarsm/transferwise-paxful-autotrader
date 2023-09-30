package com.rassame.trader.services;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Wise {
    private String id;

    private String key;
    private String url = "https://api.transferwise.com";
    private String privateKey;
    private String profileId;
    private String accountId;
    private String email;
    private String name;
    private String accountUSD;
    private String accountEUR;
    private String accountGBP;
}
