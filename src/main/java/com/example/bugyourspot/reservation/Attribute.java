package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
@Table
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

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}

