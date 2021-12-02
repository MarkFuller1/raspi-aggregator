package com.raspi.process.aggregator.service;

import com.raspi.process.aggregator.model.NodePayloadEntry;
import com.raspi.process.aggregator.repository.NodeMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NodePayloadEntryService {

    @Autowired
    NodeMessageRepository nodeMessageRepository;


    public List<String> getActiveNodes(){

    }

}
