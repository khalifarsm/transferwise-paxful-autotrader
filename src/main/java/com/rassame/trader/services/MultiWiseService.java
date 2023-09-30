package com.rassame.trader.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MultiWiseService {

    @Value("${api.wise.id}")
    private String id;
    @Value("${api.wise.mode}")
    private String mode;

    private Map<String, Wise> accounts = new HashMap<>();
    private Wise test;

    @PostConstruct
    public void setUp() {
        this.test = new Wise();
        //todo create wise accounts
    }

    public Wise getWise() {
        if (mode.equalsIgnoreCase("PROD")) {
            return accounts.get(id);
        } else {
            return test;
        }
    }

    public List<Wise> getOthers() {
        List<Wise> list = new ArrayList<>();
        if (mode.equalsIgnoreCase("PROD")) {
            for (Map.Entry<String, Wise> entry : accounts.entrySet()) {
                if (!entry.getKey().equalsIgnoreCase(id)) {
                    list.add(entry.getValue());
                }
            }
        } else {
            list.add(test);
        }
        return list;
    }
}
