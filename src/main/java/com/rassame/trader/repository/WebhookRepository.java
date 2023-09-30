package com.rassame.trader.repository;

import com.rassame.trader.domain.Webhook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebhookRepository extends CrudRepository<Webhook, Long> {
    List<Webhook> findByTradeId(String tradeId);
}
