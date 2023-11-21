package com.example.bugyourspot.reservation;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table
public class Client {

    @Id
    @SequenceGenerator(
            name = "client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_sequence"
    )
    private Long clientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int slotLength;
    private int reservationsPerSlot;

    public Client() {
    }

    public Client(Long clientId) {
        this.clientId = clientId;
    }
    
    public Client(LocalDateTime startTime, LocalDateTime endTime, int slotLength, int reservationsPerSlot) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotLength = slotLength;
        this.reservationsPerSlot = reservationsPerSlot;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getSlotLength() {
        return slotLength;
    }

    public void setSlotLength(int slotLength) {
        this.slotLength = slotLength;
    }

    public int getReservationsPerSlot() {
        return reservationsPerSlot;
    }

    public void setReservationsPerSlot(int reservationsPerSlot) {
        this.reservationsPerSlot = reservationsPerSlot;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", slotLength=" + slotLength +
                ", reservationsPerSlot=" + reservationsPerSlot +
                '}';
    }
}