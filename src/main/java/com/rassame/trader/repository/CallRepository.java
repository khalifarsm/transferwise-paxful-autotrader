package com.rassame.trader.repository;

import com.rassame.trader.domain.Call;
import com.rassame.trader.domain.TransactionStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallRepository extends CrudRepository<Call, Long> {
    List<Call> findByTransferIdAndStatusIn(String transferId, List<TransactionStatus> statuses);
    List<Call> findByTradeIdAndStatusIn(String tradeId, List<TransactionStatus> statuses);
}
