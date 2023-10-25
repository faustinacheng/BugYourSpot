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
    private Integer clientId;
    private Integer customerId;
    private LocalDateTime startTime;
    private Integer numSlots;

    // TODO: store custom values in map?

    public Reservation(){
    }

    public Reservation(Long reservationId, Integer clientId, Integer customerId, LocalDateTime startTime, Integer numSlots) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.clientId = clientId;
        this.startTime = startTime;
        this.numSlots = numSlots;
    }

    public Reservation(Integer clientId, Integer customerId, LocalDateTime startTime, Integer numSlots) {
        this.clientId = clientId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.numSlots = numSlots;
    }

    public Integer getClientId() {
        return clientId;
    }

    public Integer getCustomerId() { return customerId; }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Integer getNumSlots() {
        return numSlots;
    }

    public Long getReservationId() { return reservationId; }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

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