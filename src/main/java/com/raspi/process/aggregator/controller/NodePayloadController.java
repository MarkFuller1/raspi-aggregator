package com.raspi.process.aggregator.controller;

import com.raspi.process.aggregator.model.NodeLastState;
import com.raspi.process.aggregator.model.NodePayloadEntry;
import com.raspi.process.aggregator.repository.NodeMessageRepository;
import com.raspi.process.aggregator.service.NodePayloadEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/nodes")
@CrossOrigin(origins = "*")
public class NodePayloadController {

    @Autowired
    NodeMessageRepository nodeMessageRepository;

    @Autowired
    NodePayloadEntryService nodePayloadEntryService;

    @GetMapping("/payloads")
    public ResponseEntity<List<NodePayloadEntry>> getAllFromRepo() {
        return new ResponseEntity(StreamSupport.stream(nodeMessageRepository.findAll().spliterator(), false).collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{ip}/logs")
    public ResponseEntity<List<NodePayloadEntry>> getFewLogsFromNode(@PathVariable String ip) {
        List<NodePayloadEntry> response = nodeMessageRepository.getLastFew(ip);
        return new ResponseEntity<>(StreamSupport.stream(nodeMessageRepository.getLastFew(ip).spliterator(), false).collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, List<Pair<String, String>>>> getActiveNodes() {
        Map<String, List<Pair<String, String>>> nodeStates = nodePayloadEntryService.getActiveNodes();

        if (nodeStates.containsKey("ALIVE")) {
            List<String> activeNodes = nodeStates.get("ALIVE").stream().map(Pair::getFirst).collect(Collectors.toList());
            activeNodes.stream().forEach(node -> nodeMessageRepository.save(new NodePayloadEntry(null, node, "", "ALIVE", "", LocalDateTime.now())));
        }
        if (nodeStates.containsKey("LOST")) {
            List<String> lostNodes = nodeStates.get("LOST").stream().map(Pair::getFirst).collect(Collectors.toList());
            lostNodes.stream().forEach(node -> nodeMessageRepository.save(new NodePayloadEntry(null, node, "", "LOST", "", LocalDateTime.now())));
        }


        return new ResponseEntity<Map<String, List<Pair<String, String>>>>(nodeStates, HttpStatus.OK);
    }

    @GetMapping("/ip")
    public ResponseEntity<List<NodeLastState>> getAllIps() {
        // get all nodes
        List<String> nodes = nodeMessageRepository.getNodes();

        // get state for most recent record for each node
        List<NodeLastState> lastState = nodes.stream().map(node -> new NodeLastState(node, nodeMessageRepository.getLastState(node))).collect(Collectors.toList());
        return new ResponseEntity<List<NodeLastState>>(lastState, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<String>> validateNodes() {
        // send request to every node in list

        // if they request with 200 do nothing

        // if they dont, add a missing entry to the db

        // return the new list of nodes with "missing" removed
        return null;
    }
}
