package com.example.bugyourspot.reservation;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public void updateReservation(Long id, LocalDateTime startTime, LocalDateTime endTime) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("reservation with id " + id + " does not exist"));

        if (startTime != null && !Objects.equals(reservation.getStartTime(), startTime)) {
            // TODO: limit number of reservations per certain time slot
            reservation.setStartTime(startTime);
        }

        if (endTime != null && !Objects.equals(reservation.getEndTime(), endTime)) {
            // TODO: Check if start time < end time
            reservation.setEndTime(endTime);
        }
    }
}
