package com.hsbc.financial.domain.transaction.event;

import com.hsbc.financial.domain.transaction.entity.TransactionEvent;

/**
 * EventBus
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className EventBus
 **/
public interface EventBus {
    /**
     * 发布一个事务事件。
     * @param event 要发布的交易事件对象。
     */
    void publish(TransactionEvent event);
}
