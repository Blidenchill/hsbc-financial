package com.hsbc.financial.domain.transaction.facade;

import com.hsbc.financial.domain.transaction.entity.TransactionEvent;

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
}
