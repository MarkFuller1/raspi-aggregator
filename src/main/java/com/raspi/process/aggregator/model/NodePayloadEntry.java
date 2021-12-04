package com.raspi.process.aggregator.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "node_payload", schema = "aggregator")
public class NodePayloadEntry {
    @Id
    @Getter
    @Setter
    @GeneratedValue
    private UUID node_payload_id;

    @Getter
    @Setter
    @Column(name = "ip_address")
    private String ip_address;

    @Getter
    @Setter
    @Column(name = "timer_duration_left")
    private String timerDurationLeft;

    @Getter
    @Setter
    @Column(name = "state")
    private String state;

    @Getter
    @Setter
    @Column(name = "message")
    private String message;

    @Setter
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public String getTimestamp(){
    return this.timestamp.format(DateTimeFormatter.ofPattern("EEEE, MMM d 'at' hh:mm a"));
    }
}
