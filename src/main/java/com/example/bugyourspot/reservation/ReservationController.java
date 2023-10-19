package com.example.bugyourspot.reservation;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(
            value = "/process",
            method = RequestMethod.POST)
    public void createReservationSchema (@RequestBody Map<String, Object>[] schema) {
        reservationService.createReservationSchema(schema);
    }

    @PostMapping
    public void createReservation (@RequestBody Reservation reservation) {
        // TODO: change parameter to ReservationDTO, pass Reservation and ReservationCustomValue to reservationService

        reservationService.addNewReservation(reservation);
    }

    @DeleteMapping(path = "{reservationId}")
    public void cancelReservation(@PathVariable("reservationId") Long reservationId) {
        reservationService.deleteReservation(reservationId);
    }

    @PutMapping(path = "{reservationId}")
    public void updateReservation(
            @PathVariable("reservationId") Long reservationId,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime
    ) {
        reservationService.updateReservation(reservationId, startTime, endTime);
    }
}
