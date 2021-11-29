package com.raspi.process.aggregator.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "node_payload", schema = "aggregator")
public class NodePayloadEntry {
    @Id
    @GeneratedValue
    UUID node_payload_id;

    @Column(name = "ip_address")
    String ip_address;

    @Column(name = "timer_duration_left")
    String timerDurationLeft;

    @Column(name = "state")
    String state;

    @Column(name = "message")
    String message;

    @Column(name = "timestamp")
    LocalDateTime timestamp;
}