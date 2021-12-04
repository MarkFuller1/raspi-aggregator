package com.raspi.process.aggregator.service;

import com.raspi.process.aggregator.repository.NodeMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NodePayloadEntryService {

    @Autowired
    NodeMessageRepository nodeMessageRepository;

    @Autowired
    RestTemplate rest;

    public Map<String, List<Pair<String, String>>> getActiveNodes() {

        List<String> nodes = nodeMessageRepository.getNodes();
        Map<String, List<String>> states = new HashMap<>();
        List<CompletableFuture<Pair<String, String>>> futureResults = new ArrayList<>();

        nodes.stream().forEach(node -> {
            CompletableFuture<Pair<String, String>> result = asyncRestRequest(node, HttpMethod.GET, null);
            futureResults.add(result);
        });

        try {
            while (!futureResults.stream().allMatch(CompletableFuture::isDone)) {

                if (!futureResults.stream().filter(result -> result.isCancelled() || result.isCompletedExceptionally()).collect(Collectors.toList()).isEmpty()) {
                    throw new Exception("Some threads failed");
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // all threads completed successfully

            return (Map<String, List<Pair<String, String>>>) futureResults.stream().map(result -> {
                        try {
                            return (Pair<String, String>) result.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        return Pair.of("", "");
                    })
                    .collect(Collectors.groupingBy(e -> (String) e.getSecond()));
//            .entrySet().stream().map(e -> new AbstractMap.SimpleEntry<String, List<String>>(e.getKey(), e.getValue().stream().map(v -> v.getFirst()).collect(Collectors.toList())));

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Async(value = "threadPoolTaskExecutor")
    public <T> CompletableFuture<Pair<String, String>> asyncRestRequest(String ip, HttpMethod method, RequestEntity<T> entity) {
        try {
            ResponseEntity<String> response = rest.exchange("http://" + ip + ":8080/", method, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info(ip + " is still alive");

                return CompletableFuture.completedFuture(Pair.of(ip, "ALIVE"));
//            return new AsyncResult<String>("ALIVE");
            } else {
                log.info(ip + " is dead");
                return CompletableFuture.completedFuture(Pair.of(ip, "LOST"));
//            return new AsyncResult<String>("LOST");
            }
        } catch (Exception e) {
            log.error("And exception occurred" + e.getMessage());
            return CompletableFuture.completedFuture(Pair.of(ip, "LOST"));
        }
    }

    public static List<String> addToMapList(List<String> list, String val) {
        if (list == null || list.isEmpty()) {
            List<String> newList = new ArrayList<>();
            newList.add(val);
            return newList;
        } else {
            list.add(val);
            return list;
        }
    }
}
