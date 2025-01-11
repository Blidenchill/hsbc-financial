package com.hsbc.financial.domain.transaction.service.impl;

import com.hsbc.financial.domain.common.utils.JacksonUtil;
import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.domain.transaction.event.EventBus;
import com.hsbc.financial.domain.transaction.facade.TransactionEventFacadeService;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import com.hsbc.financial.domain.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.UUID;

/**
 * TransactionServiceImpl
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionServiceImpl
 **/
@Service
public class TransactionServiceImpl implements TransactionService {

    private final EventBus eventBus;
    private final TransactionEventFacadeService transactionEventFacadeService;

    @Autowired
    public TransactionServiceImpl(EventBus eventBus, TransactionEventFacadeService transactionEventFacadeService) {
        this.eventBus = eventBus;
        this.transactionEventFacadeService = transactionEventFacadeService;
    }

    @Transactional
    @Override
    public void processTransaction(TransactionCommand command) {
        // 创建交易事件
        TransactionEvent event = new TransactionEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setTransactionId(command.getTransactionId());
        event.setSourceAccountId(command.getSourceAccountId());
        event.setDestAccountId(command.getDestAccountId());
        event.setAmount(command.getAmount());
        event.setEventType("TRANSACTION_CREATED");
        event.setEventData(JacksonUtil.toJson(command));
        event.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        // 保存事件
        transactionEventFacadeService.save(event);
        // 发布事件
        eventBus.publish(event);
    }
}

