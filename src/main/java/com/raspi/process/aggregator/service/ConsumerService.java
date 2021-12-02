package com.raspi.process.aggregator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raspi.process.aggregator.service.model.NodePayload;
import com.raspi.process.aggregator.model.NodePayloadEntry;
import com.raspi.process.aggregator.repository.NodeMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConsumerService {

    public List<String> messages = new ArrayList<String>();

    @Autowired
    NodeMessageRepository nodeMessageRepository;

    @KafkaListener(topics = "${kafkaMessagesTopic}", groupId = "${kafkaGroupId}")
    public void consumer(String message) {
        log.info("consumed:" + message);
        try {
            ObjectMapper mapper = new ObjectMapper();
            NodePayload parsedMessage = mapper.readValue(message, NodePayload.class);
            nodeMessageRepository.save(convert(parsedMessage));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to save data:" + e.getMessage());
        }
    }

    private NodePayloadEntry convert(NodePayload parsedMessage) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime ts = LocalDateTime.parse(parsedMessage.getTimeStamp(), formatter);

        return NodePayloadEntry.builder()
                .ip_address(parsedMessage.getIP_ADDRESS())
                .state(parsedMessage.getState())
                .message(parsedMessage.getMessage())
                .timerDurationLeft(parsedMessage.getTimerDurationLeft())
                .timestamp(ts)
                .build();
    }
}