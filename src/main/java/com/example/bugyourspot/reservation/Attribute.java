package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attributeId;
    private Integer clientId;

    private String label;

    private String dataType;

    public Attribute() {
    }

    public Attribute(Integer clientId, String label, String dataType) {
        this.clientId = clientId;
        this.label = label;
        this.dataType = dataType;
    }

    public long getAttributeId() {
        return attributeId;
    }

    public long getClient() {
        return clientId;
    }

    public String getLabel() {
        return label;
    }

    public String getDataType() {
        return dataType;
    }
}

