package com.hsbc.financial.infrastructure.mq;

import com.hsbc.financial.domain.common.utils.JacksonUtil;
import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.domain.transaction.event.EventBus;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;

/**
 * LocalEventBus
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className LocalEventBus
 **/
public class LocalEventBus implements EventBus {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void publish(TransactionEvent event) {
        TransactionCommand transactionCommand = JacksonUtil.fromJson(event.getEventData(), TransactionCommand.class);
        if(Objects.isNull(transactionCommand)) {
            throw new RuntimeException("event data is null");
        }
        applicationEventPublisher.publishEvent(transactionCommand);
    }
}
