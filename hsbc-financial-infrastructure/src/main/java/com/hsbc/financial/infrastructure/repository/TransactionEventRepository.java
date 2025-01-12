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

    /**
     * 根据交易ID查找交易事件。
     * @param transactionId 交易ID。
     * @return 对应的交易事件。
     */
    TransactionEvent findByTransactionId(String transactionId);
}
