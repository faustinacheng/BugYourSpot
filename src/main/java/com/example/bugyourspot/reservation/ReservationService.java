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
import java.util.Set;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSchemaRepository reservationSchemaRepository;
    private final AttributeRepository attributeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ReservationSchemaRepository reservationSchemaRepository, AttributeRepository attributeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationSchemaRepository = reservationSchemaRepository;
        this.attributeRepository = attributeRepository;
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

    public void addNewReservationSchema(ReservationSchema reservationSchema) {
        reservationSchemaRepository.save(reservationSchema);
    }

    public void createReservationSchema(ReservationSchema reservationSchema) {
        reservationSchemaRepository.save(reservationSchema);

        Long clientId = reservationSchema.getClientId();
        Map<String, String> schema = reservationSchema.getFields();
        Set<String> mandatoryFields = Set.of("startTime", "numSlots", "customerId");


        // Loop through schema and ensure all mandatory fields are passed in
        for (String mandatoryField : mandatoryFields) {
            if (!schema.containsKey(mandatoryField)) {
                // return an error back to the client
                throw new IllegalStateException("Missing " + mandatoryField + " in Schema");
            }
        }

        // Call AttributeRepository to add (value, type) to attributes table for each custom attribute
        for (String attributeName : schema.keySet()) {
            if (mandatoryFields.contains(attributeName)) continue;

            String attributeType = schema.get(attributeName);
            // randomly generate attributeId
            Attribute attribute = new Attribute(clientId, attributeName, attributeType);
            attributeRepository.save(attribute);
        }

    }

    public void deleteReservation(Long reservationId) {
        boolean exists = reservationRepository.existsById(reservationId);
        if (!exists) {
            throw new IllegalStateException("reservation with id " + reservationId + " does not exist");
        }
        reservationRepository.deleteById(reservationId);
    }

    @Transactional
    public void updateReservation(Long reservationId, LocalDateTime startTime, Integer numSlots) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("reservation with id " + reservationId + " does not exist"));

        if (startTime != null && !Objects.equals(reservation.getStartTime(), startTime)) {
            // TODO: limit number of reservations per certain time slot
            reservation.setStartTime(startTime);
        }

        if (numSlots != null && !Objects.equals(reservation.getNumSlots(), numSlots)) {
            reservation.setNumSlots(numSlots);
        }
    }
}
