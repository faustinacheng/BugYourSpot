package com.example.bugyourspot.reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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

    @RequestMapping(
            value = "/createClient",
            method = RequestMethod.POST)
    public Long createClient (@RequestBody ClientDTO clientDTO) {
        try {
            return reservationService.createClient(clientDTO);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/getClient")
    public Client getClient(@RequestParam("clientId") Long clientId) {
        try {
            return reservationService.getClient(clientId);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/getClientReservations")
    public List<Map<String, String>> getClientReservations(@RequestParam("clientId") Long clientId) {
        try {
            return reservationService.getClientReservations(clientId);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping
    public void createReservation(@RequestBody ReservationDTO reservationDto) {
        try {
            reservationService.createReservation(reservationDto);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
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
        //System.out.println(numSlots);
        reservationService.updateReservation(reservationId, startTime, numSlots);
    }

    @GetMapping
    public List<Reservation> getReservations() {
        return reservationService.getReservations();
    }
}
