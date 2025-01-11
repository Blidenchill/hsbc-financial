package com.hsbc.financial.infrastructure.config;

import com.hsbc.financial.domain.transaction.event.EventBus;
import com.hsbc.financial.infrastructure.mq.KafkaEventBus;
import com.hsbc.financial.infrastructure.mq.LocalEventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * EventBusConfig
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className EventBusConfig
 **/
@Configuration
class EventBusConfig {

    @Bean
    @ConditionalOnProperty(name = "event.bus.type", havingValue = "local", matchIfMissing = true)
    public EventBus localBus() {
        return new LocalEventBus();
    }

    @Bean
    @ConditionalOnProperty(name = "event.bus.type", havingValue = "kafka")
    public EventBus kafkaBus() {
        return new KafkaEventBus();
    }
}
