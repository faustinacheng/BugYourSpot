package com.example.bugyourspot.reservation;
import jakarta.persistence.*;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    public Client() {
    }

    public Client(Long clientId) {
        this.clientId = clientId;
    }

}