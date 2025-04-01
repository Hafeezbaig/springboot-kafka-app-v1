# Spring Boot Kafka Project (KRaft Mode)

This is a simple Kafka-based application using Spring Boot that allows sending and consuming messages via a REST API. It uses Apache Kafka running in KRaft (Kafka Raft metadata mode) using the Bitnami Docker image.

---

## Features

- Kafka setup using Bitnami Kafka Docker image (no ZooKeeper, KRaft mode)
- Spring Boot Kafka producer and consumer
- REST API endpoint to produce messages
- Kafka listener to consume messages
- Swagger/OpenAPI documentation
- Postman collection available for quick API testing

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

docker/
└── docker-compose.yml

root/
└── kafka-postman-collection.json
```

---

## Step 1: Start Kafka with Docker Compose (KRaft Mode)

From the project root, run:

```bash
docker-compose -f docker/docker-compose.yml up -d
```

To stop Kafka:

```bash
docker-compose -f docker/docker-compose.yml down
```

To view logs:

```bash
docker-compose -f docker/docker-compose.yml logs -f
```

To enter the Kafka container:

```bash
docker exec -it kafka-container bash
```

---

## Step 2: Optional – Create Kafka Topic Manually

Kafka topics are auto-created if `KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE=true`. If needed, manually create:

```bash
docker exec -it kafka-container bash

kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic message-topic \
  --partitions 1 --replication-factor 1
```

If the topic already exists, the error can be ignored.

---

## Step 3: Run the Spring Boot Application

Check if port 9200 is free:

```bash
lsof -i :9200
```

If it is occupied, kill the process:

```bash
kill -9 <pid>
```

Then run the app:

```bash
mvn spring-boot:run
```

---

## Step 4: Send a Message (via cURL)

```bash
curl -X POST http://localhost:9200/api/produce \
  -H "Content-Type: application/json" \
  -d '{"message": "Kafka is awesome!"}'
```

---

## Step 5: Send a Message (via Postman)

You can import the `kafka-postman-collection.json` file from the project root into Postman.

Send a `POST` request to:

```
http://localhost:9200/api/produce
```

Body (JSON):

```json
{
  "message": "Kafka is awesome!"
}
```

---

## Output Logs

**Producer logs:**

```
Producing message to Kafka: Kafka is awesome!
Message sent to topic: message-topic
```

**Consumer logs:**

```
Received message from Kafka: Kafka is awesome!
```

---

## Swagger UI

You can access the API documentation at:

```
http://localhost:9200/swagger-ui/index.html
```

---

## Status

Kafka is running in KRaft mode via Docker Compose.  
Spring Boot successfully produces and consumes messages using Kafka.  
Postman and Swagger are integrated for easy testing.  
The project is ready for review and the next enhancement steps.

---

