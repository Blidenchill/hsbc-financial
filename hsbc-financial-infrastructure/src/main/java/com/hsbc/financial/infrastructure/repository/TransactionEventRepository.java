package com.hsbc.financial.infrastructure.repository;

import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TransactionRepository
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionRepository
 **/
public interface TransactionEventRepository extends JpaRepository<TransactionEvent, Long> {

    TransactionEvent findByTransactionId(String transactionId);
}
