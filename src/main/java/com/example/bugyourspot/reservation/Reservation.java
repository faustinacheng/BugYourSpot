package com.example.bugyourspot.reservation;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;

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

    private Long reservationId;
    private Long clientId;
    private Long userId;
    private LocalDateTime startTime;
    private Integer numSlots;


    public Reservation(){
    }



    public Reservation(Long reservationId, Long clientId, Long userId, LocalDateTime startTime, Integer numSlots) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.clientId = clientId;
        this.startTime = startTime;
        this.numSlots = numSlots;
    }

    public Reservation(Long clientId, Long userId, LocalDateTime startTime, Integer numSlots) {
        this.clientId = clientId;
        this.userId = userId;
        this.startTime = startTime;
        this.numSlots = numSlots;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getUserId() { return userId; }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Integer getNumSlots() {
        return numSlots;
    }

    public Long getReservationId() { return reservationId; }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setUserId(Long userId) { this.userId = userId; }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setNumSlots (Integer numSlots) {
        this.numSlots = numSlots;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", clientId=" + clientId +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", numSlots=" + numSlots +
                '}';
    }
}