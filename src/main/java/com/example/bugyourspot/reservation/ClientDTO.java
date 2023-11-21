package com.example.bugyourspot.reservation;

import java.time.LocalDateTime;
import java.util.Map;

public class ClientDTO {

    private Map<String, String> customValues;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int slotLength;
    private int reservationsPerSlot;

    public ClientDTO() {
    }

    public ClientDTO(Map<String, String> customValues, LocalDateTime startTime, LocalDateTime endTime, int slotLength, int reservationsPerSlot) {
        this.customValues = customValues;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotLength = slotLength;
        this.reservationsPerSlot = reservationsPerSlot;
    }

    public Map<String, String> getCustomValues() {
        return customValues;
    }

    public void setCustomValues(Map<String, String> customValues) {
        this.customValues = customValues;
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
}
