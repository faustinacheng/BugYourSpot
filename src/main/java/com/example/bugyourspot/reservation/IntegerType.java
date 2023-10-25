package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
public class IntegerType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId;

    private Long attributeId;

    private Integer value;

    public IntegerType() {
    }

    public IntegerType(Long reservationId, Long attributeId, String value) {
        this.reservationId = reservationId;
        this.attributeId = attributeId;
        this.value = Integer.parseInt(value);
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

    public Integer getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = Integer.parseInt(value);
    }
}

