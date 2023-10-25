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

    private Long reservationId;
    private Long clientId;
    private Long customerId;
    private LocalDateTime startTime;
    private Integer numSlots;

    // TODO: store custom values in map?

    public Reservation(){
    }

    public Reservation(Long reservationId, Long clientId, Long customerId, LocalDateTime startTime, Integer numSlots) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.clientId = clientId;
        this.startTime = startTime;
        this.numSlots = numSlots;
    }

    public Reservation(Long clientId, Long customerId, LocalDateTime startTime, Integer numSlots) {
        this.clientId = clientId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.numSlots = numSlots;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getCustomerId() { return customerId; }

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

    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setNumSlots (Integer numSlots) {
        this.numSlots = numSlots;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "clientId=" + clientId +
                ", startTime=" + startTime +
                ", numSlots=" + numSlots +
                '}';
    }
}