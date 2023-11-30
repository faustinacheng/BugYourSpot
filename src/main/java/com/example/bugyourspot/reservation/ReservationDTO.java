package com.example.bugyourspot.reservation;

import java.time.LocalDateTime;
import java.util.Map;

public class ReservationDTO {
    private Long reservationId;
    private Long clientId;
    private Long userId;
    private LocalDateTime startTime;
    private Integer numSlots;
    private Map<String, String> customValues;

    public ReservationDTO(Long clientId, Long userId, LocalDateTime startTime, Integer numSlots, Map<String, String> customValues) {
        this.clientId = clientId;
        this.userId = userId;
        this.startTime = startTime;
        this.numSlots = numSlots;
        this.customValues = customValues;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getNumSlots() {
        return numSlots;
    }

    public void setNumSlots(Integer numSlots) {
        this.numSlots = numSlots;
    }

    public Map<String, String> getCustomValues() {
        return customValues;
    }

    public void setCustomValues(Map<String, String> customValues) {
        this.customValues = customValues;
    }


}
