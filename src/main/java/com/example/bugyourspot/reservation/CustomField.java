package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
public class CustomField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attributeId;
    private Integer clientId;
    private String fieldName;
    private String fieldType; // Consider using an enum (e.g., TEXT, INTEGER, DATE)

    // TODO: getters, setters, etc.
}

