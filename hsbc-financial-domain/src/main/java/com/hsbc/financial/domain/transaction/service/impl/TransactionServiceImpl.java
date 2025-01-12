package com.hsbc.financial.domain.transaction.service.impl;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.account.facade.AccountFacadeService;
import com.hsbc.financial.domain.common.utils.JacksonUtil;
import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.domain.transaction.event.EventBus;
import com.hsbc.financial.domain.transaction.facade.TransactionEventFacadeService;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import com.hsbc.financial.domain.transaction.service.TransactionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
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

    /**
     * 事件总线，用于发布交易事件。
     */
    private final EventBus eventBus;

    /**
     * 提供交易事件的保存和发布功能。
     */
    private final TransactionEventFacadeService transactionEventFacadeService;

    /**
     * 账户外观服务，提供账户相关的操作接口。
     */
    private final AccountFacadeService accountFacadeService;

    /**
     * 构造函数，初始化TransactionServiceImpl实例。
     *
     * @param eventBus                      事件总线服务，用于发布和订阅事件。
     * @param transactionEventFacadeService 事务事件外观服务，提供事务相关的操作接口。
     */
    @Autowired
    public TransactionServiceImpl(EventBus eventBus, TransactionEventFacadeService transactionEventFacadeService, AccountFacadeService accountFacadeService) {
        this.eventBus = eventBus;
        this.transactionEventFacadeService = transactionEventFacadeService;
        this.accountFacadeService = accountFacadeService;
    }

    /**
     * 处理交易命令，创建并发布交易事件。
     *
     * @param command 交易命令对象，包含交易的详细信息。
     */
    @Transactional
    @Override
    public void processTransaction(TransactionCommand command) {
        checkTransactionCommand(command);
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

    private void checkTransactionCommand(TransactionCommand command) throws IllegalArgumentException {
        if (Objects.isNull(command)) {
            throw new IllegalArgumentException("command is null");
        }
        if (StringUtils.isBlank(command.getSourceAccountId()) || StringUtils.isBlank(command.getDestAccountId())) {
            throw new IllegalArgumentException("accountId is null");
        }
        if (command.getSourceAccountId().equals(command.getDestAccountId())) {
            throw new IllegalArgumentException("source accountId and destination accountId are the same");
        }
        Optional<Account> sourceAccount = accountFacadeService.findByAccountId(command.getSourceAccountId());
        if (sourceAccount.isEmpty()) {
            throw new IllegalArgumentException("source account not found");
        }
        Optional<Account> destAccount = accountFacadeService.findByAccountId(command.getDestAccountId());
        if (destAccount.isEmpty()) {
            throw new IllegalArgumentException("destination account not found");
        }
    }
}

