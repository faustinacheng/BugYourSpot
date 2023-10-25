package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

    public Long getReservationId() {
        return reservationId;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public LocalDateTime getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = LocalDateTime.parse(value);
    }
}

