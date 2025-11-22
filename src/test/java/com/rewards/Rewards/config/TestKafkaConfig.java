package com.rewards.Rewards.config;
import com.rewards.Rewards.events.PointsEvent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestKafkaConfig {

    @Bean
    public KafkaTemplate<String, PointsEvent> kafkaTemplate() {
        return mock(KafkaTemplate.class);
    }
}
