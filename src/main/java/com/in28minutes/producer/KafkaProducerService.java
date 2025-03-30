package com.in28minutes.producer;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.topic.name}")
    private String topicName;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        log.info("Producing message to Kafka topic [{}]: {}", topicName, message);

        // Spring Kafka returns CompletableFuture from version 3.0+
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Failed to send message [{}] to topic [{}]", message, topicName, ex);
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("✅ Message [{}] sent successfully to topic [{}], partition [{}], offset [{}]",
                        message, topicName, metadata.partition(), metadata.offset());
            }
        });
    }
}
