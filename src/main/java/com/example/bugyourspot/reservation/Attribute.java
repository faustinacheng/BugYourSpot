package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attributeId;
    private Long clientId;

    private String label;

    private String dataType;

    public Attribute() {
    }

    public Attribute(Long clientId, String label, String dataType) {
        this.clientId = clientId;
        this.label = label;
        this.dataType = dataType;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public Long getClient() {
        return clientId;
    }

    public String getLabel() {
        return label;
    }

    public String getDataType() {
        return dataType;
    }
}

