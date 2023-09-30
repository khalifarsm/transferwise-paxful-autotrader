package com.rassame.trader.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class UnirestConfig {
    @PostConstruct
    public void setUp() {
        Unirest.setObjectMapper(new ObjectMapper() {
            com.fasterxml.jackson.databind.ObjectMapper mapper
                = new com.fasterxml.jackson.databind.ObjectMapper();

            @SneakyThrows
            public String writeValue(Object value) {
                try {
                    String object = mapper.writeValueAsString(value);
                    System.out.println("sent object : " + object.replaceAll("\\R"," "));
                    return object;
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            }

            @SneakyThrows
            public <T> T readValue(String value, Class<T> valueType) {
                System.out.println("received object : " + value.replaceAll("\\R"," "));
                try {
                    return mapper.readValue(value, valueType);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            }
        });
    }
}
