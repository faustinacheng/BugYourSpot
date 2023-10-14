package com.example.bugyourspot.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public void addNewReservation(Reservation reservation) {
        Optional<Reservation> reservationOptional =
                reservationRepository.findReservationByClientId(reservation.getClientId());
        if (reservationOptional.isPresent()) {
            throw new IllegalStateException("client taken");
        }

        reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        boolean exists = reservationRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("reservation with id " + id + " does not exist");
        }
        reservationRepository.deleteById(id);
    }
}
