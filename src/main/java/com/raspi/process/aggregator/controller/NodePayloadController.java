package com.raspi.process.aggregator.controller;

import com.raspi.process.aggregator.model.NodePayloadEntry;
import com.raspi.process.aggregator.repository.NodeMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/node_payload")
public class NodePayloadController {

    @Autowired
    NodeMessageRepository nodeMessageRepository;

    @GetMapping
    public ResponseEntity<List<NodePayloadEntry>> getAllFromRepo() {
        return new ResponseEntity(StreamSupport
                .stream(nodeMessageRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }
}
