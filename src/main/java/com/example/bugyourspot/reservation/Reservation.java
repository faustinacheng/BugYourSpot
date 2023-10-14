package com.example.bugyourspot.reservation;

import java.time.LocalDateTime;

public class Reservation {
    private Integer userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Reservation(Integer userId, LocalDateTime startTime, LocalDateTime endTime) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
                "userId=" + userId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
