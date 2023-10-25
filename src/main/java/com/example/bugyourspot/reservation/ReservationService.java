package com.example.bugyourspot.reservation;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
//    private final CustomFieldRepository customFieldRepository;
    // private final CustomFields customFields;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
//        this.customFieldRepository = customFieldRepository;
        // instantiate
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

        // TODO: save custom field values to reservationCustomValueRepository

        reservationRepository.save(reservation);
    }

    public void createReservationSchema(Map<String, Object>[] schema) {
        // TODO:
            // check if all required fields provided in schema with expected types
            // update tables in CustomFieldRepository
    }

    public void deleteReservation(Long reservationId) {
        boolean exists = reservationRepository.existsById(reservationId);
        if (!exists) {
            throw new IllegalStateException("reservation with id " + reservationId + " does not exist");
        }
        reservationRepository.deleteById(reservationId);
    }

    @Transactional
    public void updateReservation(Long reservationId, LocalDateTime startTime, LocalDateTime endTime) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("reservation with id " + reservationId + " does not exist"));

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
