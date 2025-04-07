package com.in28minutes.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * KafkaConfig sets up the topic configuration with multiple partitions.
 */
@Configuration
public class KafkaConfig {

    public static final String TOPIC_NAME = "message-topic";

    @Bean
    public NewTopic topic() {
        return new NewTopic(TOPIC_NAME, 3, (short) 1); // 3 partitions, 1 replica
    }
}
