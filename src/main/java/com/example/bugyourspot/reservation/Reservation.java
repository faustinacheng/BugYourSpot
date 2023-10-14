package com.example.bugyourspot.reservation;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class Reservation {
    @Id
    @SequenceGenerator(
            name = "reservation_sequence",
            sequenceName = "reservation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservation_sequence"
    )

    private Long id;
    private Integer clientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Reservation(){
    }

    public Reservation(Long id, Integer clientId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Reservation(Integer clientId, LocalDateTime startTime, LocalDateTime endTime) {
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getClientId() {
        return clientId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "clientId=" + clientId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
