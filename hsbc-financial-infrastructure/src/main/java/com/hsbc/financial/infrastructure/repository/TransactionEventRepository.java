package com.hsbc.financial.infrastructure.repository;

import com.hsbc.financial.domain.enums.TransactionStatus;
import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * TransactionRepository
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionRepository
 **/
@Repository
public interface TransactionEventRepository extends JpaRepository<TransactionEvent, Long> {

    /**
     * 根据交易ID查找交易事件。
     * @param transactionId 交易ID。
     * @return 对应的交易事件。
     */
    TransactionEvent findByTransactionId(String transactionId);

    @Transactional
    @Modifying
    @Query("UPDATE TransactionEvent t SET t.status = :status, t.failedReason = :failedReason WHERE t.transactionId = :transactionId")
    int updateStatusByTransactionId(@Param("transactionId") String transactionId,
                                    @Param("status") TransactionStatus status,
                                    @Param("failedReason") String failedReason);


}
