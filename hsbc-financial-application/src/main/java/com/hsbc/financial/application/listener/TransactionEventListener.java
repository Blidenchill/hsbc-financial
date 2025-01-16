package com.hsbc.financial.application.listener;

import com.hsbc.financial.domain.account.service.AccountService;
import com.hsbc.financial.domain.common.exception.AccountNotFoundException;
import com.hsbc.financial.domain.common.exception.InsufficientBalanceException;
import com.hsbc.financial.domain.common.exception.TransactionNotFoundException;
import com.hsbc.financial.domain.common.utils.JacksonUtil;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import com.hsbc.financial.domain.transaction.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * TransactionEventListener
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionEventListener
 **/
@Component
@Slf4j
public class TransactionEventListener {

    /**
     * 账户服务对象，用于更新账户余额。
     */
    private final AccountService accountService;

    /**
     * 交易服务对象，用于在事务事件中处理交易相关操作。
     */
    private final TransactionService transactionService;

    /**
     * 构造函数，用于初始化TransactionEventListener对象。
     *
     * @param accountService 账户服务对象，用于在事务事件中处理账户相关操作。
     */
    @Autowired
    public TransactionEventListener(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    /**
     * 从 Kafka 订阅的 'transaction-events' 主题中处理交易事件。
     *
     * @param record 包含交易事件数据的 ConsumerRecord 对象。
     */
    @KafkaListener(topics = "transaction-events")
    public void kafkaHandleEvent(ConsumerRecord<String, String> record) {
        // 解析事件数据
        String eventData = record.value();
        TransactionCommand command = JacksonUtil.fromJson(eventData, TransactionCommand.class);
        execute(command);
    }

    /**
     * 异步处理交易命令事件
     *
     * @param transactionCommand 交易命令对象
     */
    @Async("eventTaskExecutor")
    @EventListener
    public void localHandleEvent(TransactionCommand transactionCommand) {
        execute(transactionCommand);
    }

    /**
     * 执行交易命令，更新账户余额。
     *
     * @param command 交易命令对象
     */
    private void execute(TransactionCommand command) {
        try {
            //幂等性校验,如果当前command已被处理, 则直接返回
            if (transactionService.checkTransactionProcessed(command.getTransactionId())) {
                return;
            }
            accountService.updateAccountBalances(command);
        } catch (TransactionNotFoundException e) {
            log.error("当前transactionId不存在, 请检查, command={}", JacksonUtil.toJson(command), e);
        } catch (InsufficientBalanceException | AccountNotFoundException e) {
            //当前余额不足, 业务账号不存在. 属于业务异常,mq不抛出重试.
            transactionService.changeTransactionFailed(command.getTransactionId(), e.getMessage());
        } catch (Exception e) {
            log.error("TransactionEventListener execute error", e);
            //交易的其他异常, 先更新为失败, 然后抛出异常, 让mq重试.
            transactionService.changeTransactionFailed(command.getTransactionId(), e.getMessage());
            throw e;
        }

    }


}
