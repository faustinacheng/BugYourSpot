package com.example.bugyourspot.reservation;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
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

//    @PostMapping
    @RequestMapping(
            value = "/createReservationSchema",
            method = RequestMethod.POST)
    public void createReservationSchema (@RequestBody ReservationSchema reservationSchema) {
        reservationService.createReservationSchema(reservationSchema);
//        reservationService.addNewReservationSchema(reservationSchema);
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
            @RequestParam(required = false) Integer numSlots
    ) {
        reservationService.updateReservation(reservationId, startTime, numSlots);
    }
}
