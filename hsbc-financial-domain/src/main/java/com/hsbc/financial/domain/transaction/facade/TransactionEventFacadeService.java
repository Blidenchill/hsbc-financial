package com.hsbc.financial.domain.transaction.facade;

import com.hsbc.financial.domain.enums.TransactionStatus;
import com.hsbc.financial.domain.transaction.entity.TransactionEvent;

import java.util.Optional;

/**
 * TransactionEventFacadeSerive
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionEventFacadeSerive
 **/
public interface TransactionEventFacadeService {
    /**
     * 保存交易事件。
     * @param transactionEvent 交易事件对象。
     */
    void save(TransactionEvent transactionEvent);

    void updateStatusByTransactionId(String transactionId, TransactionStatus status, String failReason);

    Optional<TransactionEvent> findByTransactionId(String transactionId);
}
