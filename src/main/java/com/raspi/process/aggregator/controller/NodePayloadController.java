package com.raspi.process.aggregator.controller;

import com.raspi.process.aggregator.model.NodePayloadEntry;
import com.raspi.process.aggregator.repository.NodeMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/node_payload")
@CrossOrigin(origins = "*")
public class NodePayloadController {

    @Autowired
    NodeMessageRepository nodeMessageRepository;

    @GetMapping
    public ResponseEntity<List<NodePayloadEntry>> getAllFromRepo() {
        return new ResponseEntity(StreamSupport.stream(nodeMessageRepository.findAll().spliterator(), false).collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/last_few/{ip}")
    public ResponseEntity<List<NodePayloadEntry>> getAllFromTodaySorted(@PathVariable String ip) {
        return new ResponseEntity<>(StreamSupport.stream(nodeMessageRepository.getLastFew(ip).spliterator(), false).collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/nodes")
    public ResponseEntity<List<String>> getNodes() {

        return new ResponseEntity<>(nodeMessageRepository.getNodes(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<String>> validateNodes(){
        // send request to every node in list

        // if they request with 200 do nothing

        // if they dont, add a missing entry to the db

        // return the new list of nodes with "missing" removed
        return null;
    }
}
