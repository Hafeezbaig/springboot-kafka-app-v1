package com.in28minutes.producer;

import org.apache.kafka.clients.producer.RecordMetadata;
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

    /**
     * Sends a message to Kafka using a key to control partitioning.
     * @param message The message payload to send
     */
    public void sendMessage(String message) {
        log.info("Producing message to Kafka topic [{}]: {}", topicName, message);

        // Use key-based logic to determine partition
        String key = generateKey(message);  // e.g., first word as key

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, key, message);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send message [{}] with key [{}] to topic [{}]", message, key, topicName, ex);
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Message [{}] with key [{}] sent successfully to topic [{}], partition [{}], offset [{}]",
                        message, key, topicName, metadata.partition(), metadata.offset());
            }
        });
    }

    /**
     * Generates a key based on message content for partitioning logic.
     * @param message The message payload
     * @return A derived key for Kafka partitioning
     */
    private String generateKey(String message) {
        if (message == null || message.isBlank()) return "default";
        return message.split(" ")[0].toLowerCase();  // Simple logic: take first word as key
    }
}
