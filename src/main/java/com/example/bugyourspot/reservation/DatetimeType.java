package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class DatetimeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId;

    private Long attributeId;

    private LocalDateTime value;

    public DatetimeType() {
    }

    public DatetimeType(Long reservationId, Long attributeId, String value) {
        this.reservationId = reservationId;
        this.attributeId = attributeId;
        this.value = LocalDateTime.parse(value);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public LocalDateTime getValue() {
        return value;
    }

    public void setValue(LocalDateTime value) {
        this.value = value;
    }
}

