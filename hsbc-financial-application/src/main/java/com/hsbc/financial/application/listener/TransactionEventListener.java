package com.hsbc.financial.application.listener;

import com.hsbc.financial.domain.account.service.AccountService;
import com.hsbc.financial.domain.common.utils.JacksonUtil;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * TransactionEventListener
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionEventListener
 **/
@Component
public class TransactionEventListener {
    @Autowired
    AccountService accountService;

    @KafkaListener(topics = "transaction-events")
    public void kafkaHandleEvent(ConsumerRecord<String, String> record) {
        // 解析事件数据
        String eventData = record.value();
        TransactionCommand command= JacksonUtil.fromJson(eventData, TransactionCommand.class);
        execute(command);
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void localHandleEvent(TransactionCommand transactionCommand) {
        execute(transactionCommand);
    }

    private void execute(TransactionCommand command) {
        accountService.updateAccountBalances(command);
    }


}
