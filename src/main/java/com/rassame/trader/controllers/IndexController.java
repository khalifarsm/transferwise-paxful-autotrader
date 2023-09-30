package com.rassame.trader.controllers;

import com.rassame.trader.services.WiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final WiseService wiseService;

    @GetMapping("/buy")
    public String buy() {
        return "buy";
    }

    @GetMapping("/sellusd")
    public String sellUSD() {
        return "sellUSD";
    }

    @GetMapping("/sell")
    public String sell() {
        return "sell";
    }

    @PostMapping("/buy")
    public String buyPost(String trade, String transfer) {
        wiseService.buyUSDT(trade, transfer);
        return "redirect:/complete";
    }

    @PostMapping("/sellusd")
    public String sellUSDPost(String trade, String accountNumber, String routingNumber) {
        wiseService.sellUSDT(trade, accountNumber, routingNumber);
        return "redirect:/complete";
    }

    @PostMapping("/sell")
    public String sellPost(String trade, String email) {
        wiseService.sellUSDT(trade, email);
        return "redirect:/complete";
    }

    @GetMapping("/complete")
    public String complete() {
        return "done";
    }
}
