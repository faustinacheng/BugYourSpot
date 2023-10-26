package com.example.bugyourspot.reservation;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table
public class ReservationSchema {

    @Id
    @SequenceGenerator(
            name = "client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_sequence"
    )
    private Long clientId;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "schema_fields",
//            joinColumns = @JoinColumn(name = "schema_id"))
//    @MapKeyColumn(name = "field_key")
//    @Column(name = "field_value")
//    private Map<String, String> fields = new HashMap<>();


    public ReservationSchema() {
    }


    public ReservationSchema(Long clientId, Map<String, String> fields) {
        this.clientId = clientId;
//        this.fields = fields;
    }

//    public ReservationSchema(Map<String, String> fields) {
//        this.fields = fields;
//    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

//    public Map<String, String> getFields() {
//        return fields;
//    }

//    public void setFields(Map<String, String> fields) {
//        this.fields = fields;
//    }

    @Override
    public String toString() {
        return "ReservationSchema{" +
                "clientId=" + clientId +
                '}';
    }

}