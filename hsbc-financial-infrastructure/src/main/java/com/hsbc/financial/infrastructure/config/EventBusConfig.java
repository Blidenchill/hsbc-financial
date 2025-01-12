package com.hsbc.financial.infrastructure.config;

import com.hsbc.financial.domain.transaction.event.EventBus;
import com.hsbc.financial.infrastructure.mq.KafkaEventBus;
import com.hsbc.financial.infrastructure.mq.LocalEventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * EventBusConfig
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className EventBusConfig
 **/
@Configuration
class EventBusConfig {

    /**
     * 创建本地事件总线实例。
     * 当 'event.bus.type' 属性的值为 'local' 或未定义时，会调用此方法。
     */
    @Bean
    @ConditionalOnProperty(name = "event.bus.type", havingValue = "local", matchIfMissing = true)
    public EventBus localBus() {
        return new LocalEventBus();
    }

    /**
     * 在事件总线类型为 Kafka 时，创建并返回一个 KafkaEventBus 实例。
     * @return 一个 KafkaEventBus 实例。
     */
    @Bean
    @ConditionalOnProperty(name = "event.bus.type", havingValue = "kafka")
    public EventBus kafkaBus(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaEventBus(kafkaTemplate);
    }
}
