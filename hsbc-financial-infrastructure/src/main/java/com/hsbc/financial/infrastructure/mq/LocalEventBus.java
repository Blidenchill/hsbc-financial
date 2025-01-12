package com.hsbc.financial.infrastructure.mq;

import com.hsbc.financial.domain.common.exception.InfrastructureException;
import com.hsbc.financial.domain.common.utils.JacksonUtil;
import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.domain.transaction.event.EventBus;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import org.apache.commons.lang3.StringUtils;
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
    /**
     * 自动注入的应用事件发布者，用于发布交易命令事件。
     */
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    /**
     * 发布交易事件。
     * @param event 交易事件对象。
     */
    @Override
    public void publish(TransactionEvent event) {
        if(Objects.isNull(event) || StringUtils.isBlank(event.getEventData())) {
            throw new InfrastructureException("event data is null");
        }
        TransactionCommand transactionCommand = JacksonUtil.fromJson(event.getEventData(), TransactionCommand.class);
        if(Objects.isNull(transactionCommand)) {
            throw new InfrastructureException("transactionCommand is null");
        }
        applicationEventPublisher.publishEvent(transactionCommand);
    }
}
