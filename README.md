# Spring Boot Kafka Project (KRaft Mode)

This is a simple Kafka-based application using Spring Boot that allows sending and consuming messages via a REST API. It uses Apache Kafka running in KRaft (Kafka Raft metadata mode) using the Bitnami Docker image.

---

## Features

- Kafka setup using Bitnami Kafka Docker image (no Zookeeper, KRaft mode)
- Spring Boot Kafka producer and consumer setup
- REST endpoint to produce messages
- Kafka listener to consume messages
- Swagger/OpenAPI support

---

## Tech Stack

- Java 21
- Spring Boot 3.4.4
- Spring Kafka
- Apache Kafka 3.8.1
- Docker (Bitnami Kafka image)
- Maven

---

## Project Structure

```
src/
├── controller/MessageController.java
├── dto/MessageRequest.java
├── producer/KafkaProducerService.java
├── consumer/KafkaConsumerService.java
└── resources/application.properties
```

---

## Step 1: Run Kafka in Docker (KRaft mode)

```bash
docker run -d --rm --name kafka-container \
  -p 9092:9092 \
  -e KAFKA_KRAFT_CLUSTER_ID=abcdefghijklmnopqrstuv \
  -e KAFKA_CFG_NODE_ID=1 \
  -e KAFKA_CFG_PROCESS_ROLES=broker,controller \
  -e KAFKA_CFG_LISTENERS=PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093 \
  -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
  bitnami/kafka:latest
```

---

## Step 2: Create Kafka Topic (Optional if auto-created)

```bash
docker exec -it kafka-container bash

kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic message-topic \
  --partitions 1 --replication-factor 1
```

If the topic already exists, it will throw an error which can be ignored.

---

## Step 3: Run the Spring Boot Application

```bash
mvn spring-boot:run
```

Make sure port 9200 is free. If it's occupied, you can change the port in `application.properties`.

---

## Step 4: Send a Message Using cURL

```bash
curl -X POST http://localhost:9200/api/produce \
  -H "Content-Type: application/json" \
  -d '{"message": "Kafka is awesome!"}'
```

---

## Output Verification

- In Producer logs:

  ```
  Producing message to Kafka: Kafka is awesome!
  Message sent to topic: message-topic
  ```

- In Consumer logs:

  ```
  Received message from Kafka: Kafka is awesome!
  ```

---

## application.properties

```properties
spring.kafka.bootstrap-servers=localhost:9092
app.topic.name=message-topic
```

---

## API Documentation

You can view and test the API through Swagger UI:

```
http://localhost:9200/swagger-ui/index.html
```

---

## Cleaning Up

To stop Kafka:

```bash
docker stop kafka-container
```

---

## Status

The Kafka integration is complete and working. Messages can be sent via REST and consumed by the Spring Boot application.

---

