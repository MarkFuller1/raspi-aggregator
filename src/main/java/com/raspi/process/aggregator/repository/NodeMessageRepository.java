package com.raspi.process.aggregator.repository;

import com.raspi.process.aggregator.model.NodePayloadEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeMessageRepository extends CrudRepository<NodePayloadEntry, String> {

    @Query(value = "select * from aggregator.node_payload where ip_address = ?1 order by timestamp limit 25", nativeQuery = true)
    public List<NodePayloadEntry> getLastFew(String ip);

    @Query(value= "select distinct ip_address from aggregator.node_payload", nativeQuery = true)
    public List<String> getNodes();
}