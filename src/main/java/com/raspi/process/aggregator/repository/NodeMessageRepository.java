package com.raspi.process.aggregator.repository;

import com.raspi.process.aggregator.model.NodePayloadEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeMessageRepository extends CrudRepository<NodePayloadEntry, String> {
}