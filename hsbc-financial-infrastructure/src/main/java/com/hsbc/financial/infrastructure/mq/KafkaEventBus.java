package com.hsbc.financial.infrastructure.mq;

import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.domain.transaction.event.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * KafkaEventBus
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className KafkaEventBus
 **/
public class KafkaEventBus implements EventBus {

    /**
     * Kafka 主题名称，用于发布交易事件。
     */
    @Value("${event.bus.kafka.topic}")
    private String kafkaTopic;
    /**
     * 用于发送事件的 Kafka 模板对象。
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 构造 KafkaEventBus 对象并注入 KafkaTemplate。
     * @param kafkaTemplate 用于发送消息的 KafkaTemplate 实例。
     */
    @Autowired
    public KafkaEventBus(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }
    /**
     * 发布交易事件到 Kafka 主题。
     * @param event 交易事件对象，包含事件 ID 和事件数据。
     */
    @Override
    public void publish(TransactionEvent event) {
        kafkaTemplate.send(kafkaTopic, event.getEventId(), event.getEventData());
    }
}
