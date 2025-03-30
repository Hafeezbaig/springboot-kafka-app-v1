package com.in28minutes.controller;

import com.in28minutes.dto.MessageRequest;
import com.in28minutes.producer.KafkaProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final KafkaProducerService kafkaProducer;

    public MessageController(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Operation(summary = "Send a message to Kafka topic")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Message accepted for delivery",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    @PostMapping("/produce")
    public ResponseEntity<?> sendMessage(
            @RequestBody(
                    description = "Message payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MessageRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody MessageRequest request
    ) {
        kafkaProducer.sendMessage(request.message());

        return ResponseEntity.accepted().body(Map.of(
                "statusCode", 202,
                "info", "Message sent to Kafka successfully"
        ));
    }
}
