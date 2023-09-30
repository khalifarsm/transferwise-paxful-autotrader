package com.rassame.trader.client.paxful;

import com.google.common.hash.Hashing;
import com.mashape.unirest.http.Unirest;
import com.rassame.trader.config.UnirestConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
public class BasePaxfulClient {

    @Value("${api.paxful.secret}")
    private String secret;
    @Value("${api.paxful.key}")
    private String key;
    @Value("${api.paxful.url}")
    private String url;
    private final UnirestConfig unirestConfig;

    @SneakyThrows
    public Object post(Map<String, Object> params, String path, Class cla) {
        String body = mapToString(params);
        body += "&apiseal=" + calcHmacSha256(body, secret);
        return Unirest.post(url + path)
                .header("Content-Type", "text/plain")
                .header("Accept", "application/json")
                .body(body)
                .asObject(cla)
                .getBody();
    }

    public String calcHmacSha256(String data, String key) {
        return Hashing.hmacSha256(key.getBytes(StandardCharsets.UTF_8)).hashString(data, StandardCharsets.UTF_8).toString();
    }

    public String mapToString(Map<String, Object> map) {
        Long now = new Date().getTime();
        String text = "apikey=" + key + "&nonce=" + now;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            text += "&" + entry.getKey() + "=" + entry.getValue();
        }
        return text;
    }
}
