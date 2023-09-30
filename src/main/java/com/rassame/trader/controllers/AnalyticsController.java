package com.rassame.trader.controllers;

import com.rassame.trader.domain.Transaction;
import com.rassame.trader.dto.AnalyticsDTO;
import com.rassame.trader.exceptions.InvalidInputException;
import com.rassame.trader.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final TransactionRepository transactionRepository;

    @GetMapping("/total")
    public AnalyticsDTO getAnalytics(String start, String code) {
        if (!code.equals("confused")) {
            throw new InvalidInputException();
        }
        start+=" 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(start, formatter);

        List<Transaction> transactionList = transactionRepository.findByCreatedAfter(dateTime);

        Double received = 0.0;
        Double sent = 0.0;
        for (Transaction transaction : transactionList) {
            received += transaction.getReceived();
            sent += transaction.getSent();
        }

        return new AnalyticsDTO()
                .setReceived(received)
                .setSent(sent);
    }
}
