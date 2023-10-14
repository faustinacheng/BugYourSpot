package com.example.bugyourspot.reservation;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @GetMapping
    public List<Reservation> getReservations() {
        return List.of(new Reservation(4156, LocalDateTime.now(), LocalDateTime.now()));
    }
}
