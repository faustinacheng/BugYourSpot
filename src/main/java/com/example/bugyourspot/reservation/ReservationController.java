package com.example.bugyourspot.reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getReservations() {
        return reservationService.getReservations();
    }

    @PostMapping
    public void createReservation (@RequestBody Reservation reservation) {
        reservationService.addNewReservation(reservation);
    }

    @DeleteMapping(path = "{id}")
    public void cancelReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
    }

    @PutMapping(path = "{id}")
    public void updateReservation(
            @PathVariable("id") Long id,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime
    ) {
        reservationService.updateReservation(id, startTime, endTime);
    }
}
