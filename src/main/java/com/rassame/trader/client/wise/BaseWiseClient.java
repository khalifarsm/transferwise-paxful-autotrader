package com.rassame.trader.client.wise;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.rassame.trader.config.UnirestConfig;
import com.rassame.trader.services.MultiWiseService;
import com.rassame.trader.services.Wise;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@RequiredArgsConstructor
public class BaseWiseClient {

    protected final MultiWiseService multiWiseService;
    private final UnirestConfig unirestConfig;

    @SneakyThrows
    public Object get(String params, String path, Class cla) {
        HttpResponse<String> response = Unirest.get(multiWiseService.getWise().getUrl() + path + params)
                .header("Content-Type", "text/plain")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + multiWiseService.getWise().getKey())
                .asObject(cla);
        if (response.getStatus() == 403) {

            String approval = response.getHeaders().get("x-2fa-approval").get(0);
            HttpResponse<String> response2 = Unirest.get(multiWiseService.getWise().getUrl() + path + params)
                    .header("Content-Type", "text/plain")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + multiWiseService.getWise().getKey())
                    .header("X-Signature", signe(approval))
                    .header("x-2fa-approval", approval)
                    .asObject(cla);
            return response2.getBody();
        }
        return response.getBody();
    }

    @SneakyThrows
    public Object get(String params, String path, Class cla, Wise wise) {
        HttpResponse<String> response = Unirest.get(wise.getUrl() + path + params)
                .header("Content-Type", "text/plain")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + wise.getKey())
                .asObject(cla);
        if (response.getStatus() == 403) {

            String approval = response.getHeaders().get("x-2fa-approval").get(0);
            HttpResponse<String> response2 = Unirest.get(wise.getUrl() + path + params)
                    .header("Content-Type", "text/plain")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + wise.getKey())
                    .header("X-Signature", signe(approval, wise))
                    .header("x-2fa-approval", approval)
                    .asObject(cla);
            return response2.getBody();
        }
        return response.getBody();
    }

    @SneakyThrows
    public Object post(Object body, String path, Class cla) {
        HttpResponse<String> response = Unirest.post(multiWiseService.getWise().getUrl() + path)
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .header("Authorization", "Bearer " + multiWiseService.getWise().getKey())
                .body(body)
                .asObject(cla);
        if (response.getStatus() == 403) {
            String approval = response.getHeaders().get("x-2fa-approval").get(0);
            HttpResponse<String> response2 = Unirest.post(multiWiseService.getWise().getUrl() + path)
                    .header("Content-Type", "application/json")
                    .header("Accept", "*/*")
                    .header("Authorization", "Bearer " + multiWiseService.getWise().getKey())
                    .header("X-Signature", signe(approval))
                    .header("x-2fa-approval", approval)
                    .body(body)
                    .asObject(cla);
            return response2.getBody();
        }
        return response.getBody();
    }

    private PKCS8EncodedKeySpec getPrivateKey() throws IOException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(multiWiseService.getWise().getPrivateKey().getBytes()));
        return spec;
    }

    @SneakyThrows
    private String signe(String approval) {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        KeyFactory kf = KeyFactory.getInstance("RSA");
        privateSignature.initSign(kf.generatePrivate(getPrivateKey()));
        privateSignature.update(approval.getBytes("UTF-8"));
        byte[] s = privateSignature.sign();
        String encodedMessage = Base64.getEncoder().encodeToString(s);
        return encodedMessage;
    }

    private PKCS8EncodedKeySpec getPrivateKey(Wise wise) throws IOException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(wise.getPrivateKey().getBytes()));
        return spec;
    }

    @SneakyThrows
    private String signe(String approval, Wise wise) {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        KeyFactory kf = KeyFactory.getInstance("RSA");
        privateSignature.initSign(kf.generatePrivate(getPrivateKey(wise)));
        privateSignature.update(approval.getBytes("UTF-8"));
        byte[] s = privateSignature.sign();
        String encodedMessage = Base64.getEncoder().encodeToString(s);
        return encodedMessage;
    }
}
