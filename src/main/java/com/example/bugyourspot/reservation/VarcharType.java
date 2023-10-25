package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
public class VarcharType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId;

    private Long attributeId;

    private String value;

    public VarcharType() {
    }

    public VarcharType(Long reservationId, Long attributeId, String value) {
        this.reservationId = reservationId;
        this.attributeId = attributeId;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

