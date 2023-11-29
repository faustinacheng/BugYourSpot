package com.example.bugyourspot.reservation;

import java.time.LocalTime;
import java.util.Map;

public class ClientDTO {

    private Map<String, String> customValues;
    private LocalTime startTime;
    private LocalTime endTime;
    private int slotLength;
    private int reservationsPerSlot;

    public ClientDTO() {
    }

    public ClientDTO(Map<String, String> customValues, LocalTime startTime, LocalTime endTime, int slotLength, int reservationsPerSlot) {
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
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
