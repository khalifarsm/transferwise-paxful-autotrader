package com.rassame.trader.client.currency;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CurrencyClient {

    @Value("${api.currency.converter.url}")
    private String url;

    @SneakyThrows
    public Double get(String currency) {
        url = url.replace("[[CURRENCY]]", currency);
        String pair = currency + "_USD";
        String response = Unirest.get(url).asString().getBody();
        System.out.println("received value for " + currency + " is " + response);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(response);
        return node.get(pair).asDouble();
    }
}
