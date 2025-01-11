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
    void publish(TransactionEvent event);
}
