package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
public class ReservationCustomValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="reservation_id")
    private Reservation reservation;
    @ManyToOne
    @JoinColumn(name="custom_field_id")
    private Attribute customField;
    private String value; // Store as a string, and convert based on fieldType when necessary

    // TODO: getters, setters, etc.
}

