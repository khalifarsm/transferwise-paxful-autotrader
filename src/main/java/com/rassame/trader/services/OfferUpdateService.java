package com.rassame.trader.services;

import com.rassame.trader.client.paxful.OfferClient;
import com.rassame.trader.client.paxful.protocol.Offer;
import com.rassame.trader.client.wise.StatementClient;
import com.rassame.trader.client.wise.protocol.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferUpdateService {

    @Value("${trade.price.max}")
    private double maxPrice;
    @Value("${trade.price.min}")
    private double minPrice;

    @Value("${trade.sell.max}")
    private double maxSell;
    @Value("${trade.sell.min}")
    private double minSell;

    private final OfferClient offerClient;
    private final StatementClient statementClient;

    @Scheduled(fixedDelay = 6 * 60 * 60 * 1000, initialDelay = 1000)
    public void update() {
        Double price = offerClient.getOffers("buy", "USD")
                .getData()
                .getOffers()
                .stream()
                //.filter(o -> o.getMax() > 20)
                .mapToDouble(Offer::getPrice)
                .min().orElse(1);
        Double rate = (price - 1) * 100;
        rate = rate - 1;
        if (rate > maxPrice) {
            rate = maxPrice;
        }
        if (rate < minPrice) {
            rate = minPrice;
        }

        rate = rate * Math.pow(10, 2);
        rate = Math.floor(rate);
        rate = rate / Math.pow(10, 2);

        log.info("new rate is {}", rate);
        offerClient.changeOffersRate("fjGCuaAk3zi", rate);
        offerClient.changeOffersRate("ff4TSX1HFZH", rate);
        offerClient.changeOffersRate("ByJDM9osEdk", rate);
        log.info("all offers updated");
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000, initialDelay = 1000)
    public void updateSell() {
        log.info("updating sell offers");
        update("USD", "WYVNh5aR56a");
        update("EUR", "xsf8f8GuHjj");
    }

    private void update(String currency, String id) {
        log.info("update sell offer {} {}", currency, id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(240);
        Statement statement = statementClient.getStatement(currency, start.format(formatter), now.format(formatter));
        Double balance = statement.getTransactions().get(0).getRunningBalance().getValue();
        Boolean active = true;
        if (balance > maxSell) {
            balance = maxSell;
        }
        if (balance < minSell) {
            active = false;
        } else {
            offerClient.changeOffersRange(id, balance);
        }
        offerClient.active(id, active);
        log.info("updated max={} active={}", balance, active);
    }
}
