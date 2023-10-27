package com.example.bugyourspot.reservation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(
            value = "/createClient",
            method = RequestMethod.POST)
    public void createClient (@RequestBody ClientDTO clientDTO) {
        reservationService.createClient(clientDTO);
    }

    @RequestMapping(
            value = "/getClients",
            method = RequestMethod.GET)
    public List<Client> getClients() {
        return reservationService.getClients();
    }

//    @RequestMapping(
//            value = "/getClientReservations",
//            method = RequestMethod.GET)
//    @ResponseBody
//    public List<Reservation> getClientReservations(@RequestParam Long clientId) {
//        return reservationService.getClientReservations(clientId);
//    }

    @GetMapping("/getClientReservations")
    public List<Map<String, String>> getClientReservations(@RequestParam("clientId") Long clientId) {
        return reservationService.getClientReservations(clientId);
    }

    @PostMapping
    public void createReservation(@RequestBody ReservationDTO reservationDto) {
        reservationService.createReservation(reservationDto);
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
