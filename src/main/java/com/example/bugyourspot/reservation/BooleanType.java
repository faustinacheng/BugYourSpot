package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
public class BooleanType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId;

    private Long attributeId;

    private Boolean value;

    public BooleanType() {
    }

    public BooleanType(Long reservationId, Long attributeId, String value) {
        this.reservationId = reservationId;
        this.attributeId = attributeId;
        if (value.equals("true")) {
            this.value = true;
        } else {
            this.value = false;
        }
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

    public Boolean getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value.equals("true")) {
            this.value = true;
        } else {
            this.value = false;
        }
    }
}

