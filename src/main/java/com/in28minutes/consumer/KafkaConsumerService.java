package com.in28minutes.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "message-topic", groupId = "in28-group")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("📥 Received message from Kafka: " + record.value());
    }
}
