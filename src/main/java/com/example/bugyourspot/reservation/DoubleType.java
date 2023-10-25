package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
public class DoubleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId;

    private Long attributeId;

    private Double value;

    public DoubleType() {
    }

    public DoubleType(Long reservationId, Long attributeId, String value) {
        this.reservationId = reservationId;
        this.attributeId = attributeId;
        this.value = Double.parseDouble(value);
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

    public Double getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = Double.parseDouble(value);
    }
}

