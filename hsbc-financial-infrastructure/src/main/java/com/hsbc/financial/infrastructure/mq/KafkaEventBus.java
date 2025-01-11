package com.hsbc.financial.infrastructure.mq;

import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.domain.transaction.event.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * KafkaEventBus
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className KafkaEventBus
 **/
public class KafkaEventBus implements EventBus {

    @Autowired
    private  KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaEventBus() {

    }
    @Override
    public void publish(TransactionEvent event) {
        kafkaTemplate.send("transaction-events", event.getEventId(), event.getEventData());
    }
}
