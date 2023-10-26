package com.example.bugyourspot.reservation;

import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final AttributeRepository attributeRepository;

    private final VarcharTypeRepository varcharTypeRepository;

    private final DatetimeTypeRepository datetimeTypeRepository;

    private final DoubleTypeRepository doubleTypeRepository;

    private final IntegerTypeRepository integerTypeRepository;

    private final BooleanTypeRepository booleanTypeRepository;

    @Autowired
    public ReservationService(
            ReservationRepository reservationRepository,
            ClientRepository clientRepository,
            AttributeRepository attributeRepository,
            VarcharTypeRepository varcharTypeRepository,
            DatetimeTypeRepository datetimeTypeRepository,
            DoubleTypeRepository doubleTypeRepository,
            IntegerTypeRepository integerTypeRepository,
            BooleanTypeRepository booleanTypeRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.attributeRepository = attributeRepository;
        this.varcharTypeRepository = varcharTypeRepository;
        this.datetimeTypeRepository = datetimeTypeRepository;
        this.doubleTypeRepository = doubleTypeRepository;
        this.integerTypeRepository = integerTypeRepository;
        this.booleanTypeRepository = booleanTypeRepository;
    }

    @GetMapping
    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public void createClient(ClientDTO clientDTO) {
        Client client = new Client();
        clientRepository.save(client);
        Long clientId = client.getClientId();
        Map<String, String> schema = clientDTO.getCustomValues();

        // Call AttributeRepository to add (value, type) to attributes table for each custom attribute
        for (String attributeName : schema.keySet()) {
            String attributeType = schema.get(attributeName);
            // randomly generate attributeId
            Attribute attribute = new Attribute(clientId, attributeName, attributeType);
            attributeRepository.save(attribute);
        }
    }

    public List<Client> getClients () {
        return clientRepository.findAll();
    }

    public List<Map<String, String>> getClientReservations(Long clientId) {
        List<Map<String, String>> results = new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findByClientId(clientId);
        List<Attribute> attributes = attributeRepository.findByClientId(clientId);

        for (Reservation reservation: reservations) {
            Long reservationId = reservation.getReservationId();
            Map<String, String> entry = new HashMap<String, String>();
            entry.put("reservationId", reservation.getReservationId().toString());
            entry.put("startTime", reservation.getStartTime().toString());
            entry.put("numSlots", reservation.getNumSlots().toString());
            entry.put("clientId", reservation.getClientId().toString());
            entry.put("userId", reservation.getUserId().toString());

            for (Attribute attribute: attributes) {
                String dataType = attribute.getDataType();
                String label = attribute.getLabel();
                Long attributeId = attribute.getAttributeId();

                switch (dataType) {
                    case "DATETIME" -> {
                        DatetimeType data = datetimeTypeRepository.findByReservationIdAndAttributeId(reservationId, attributeId);
                        entry.put(label, data.getValue().toString());
                    }
                    case "VARCHAR" -> {
                        VarcharType data = varcharTypeRepository.findByReservationIdAndAttributeId(reservationId, attributeId);
                        entry.put(label, data.getValue());
                    }
                    case "INTEGER" -> {
                        IntegerType data = integerTypeRepository.findByReservationIdAndAttributeId(reservationId, attributeId);
                        entry.put(label, data.getValue().toString());
                    }
                    case "BOOLEAN" -> {
                        BooleanType data = booleanTypeRepository.findByReservationIdAndAttributeId(reservationId, attributeId);
                        entry.put(label, data.getValue().toString());
                    }
                    case "DOUBLE" -> {
                        DoubleType data = doubleTypeRepository.findByReservationIdAndAttributeId(reservationId, attributeId);
                        entry.put(label, data.getValue().toString());
                    }
                }
            }

            results.add(entry);
        }
        return results;
    }

    @Transactional
    public void createReservation(ReservationDTO reservationDTO) {
        Long clientId = reservationDTO.getClientId();

        // Save to reservation table
        Reservation reservation = new Reservation(clientId, reservationDTO.getUserId(), reservationDTO.getStartTime(), reservationDTO.getNumSlots());
        reservationRepository.save(reservation);

        Long reservationId = reservation.getReservationId();
        Map<String, String> customValues = reservationDTO.getCustomValues();

        // Handle customValues
        // TODO: validate all fields passed in
        List<Attribute> attributes = attributeRepository.findByClientId(clientId);
        for (Attribute attribute : attributes) {
            String label = attribute.getLabel();

            if (!customValues.containsKey(label)) {
                throw new IllegalArgumentException("Missing attribute: " + label);
            }

            String dataType = attribute.getDataType();
            String value = customValues.get(label);

            // Using dataType, decide which table/repository to use
            if (dataType.equals("DATETIME")) {
                DatetimeType datetimeType = new DatetimeType(reservationId, attribute.getAttributeId(), value);
                datetimeTypeRepository.save(datetimeType);

            } else if (dataType.equals("VARCHAR")) {
                VarcharType varcharType =  new VarcharType(reservationId, attribute.getAttributeId(), value);
                varcharTypeRepository.save(varcharType);

            } else if (dataType.equals("INTEGER")) {
                IntegerType integerType =  new IntegerType(reservationId, attribute.getAttributeId(), value);
                integerTypeRepository.save(integerType);

            } else if (dataType.equals("BOOLEAN")) {
                BooleanType booleanType =  new BooleanType(reservationId, attribute.getAttributeId(), value);
                booleanTypeRepository.save(booleanType);

            } else if (dataType.equals("DOUBLE")) {
                DoubleType doubleType =  new DoubleType(reservationId, attribute.getAttributeId(), value);
                doubleTypeRepository.save(doubleType);

            }
        }
    }

    public void deleteReservation(Long reservationId) {
        boolean exists = reservationRepository.existsById(reservationId);
        if (!exists) {
            throw new IllegalStateException("reservation with id " + reservationId + " does not exist");
        }

        Reservation reservation = reservationRepository.findByReservationId(reservationId);

        // get the attributes associated with this reservation (based on which client it belongs to)
        List<Attribute> attributes = attributeRepository.findByClientId(reservation.getClientId());
        for (Attribute attribute: attributes){
            String dataType = attribute.getDataType();
            switch (dataType) {
                case "DATETIME" -> datetimeTypeRepository.deleteById(reservationId);
                case "VARCHAR" -> varcharTypeRepository.deleteById(reservationId);
                case "INTEGER" -> integerTypeRepository.deleteById(reservationId);
                case "BOOLEAN" -> booleanTypeRepository.deleteById(reservationId);
                case "DOUBLE" -> doubleTypeRepository.deleteById(reservationId);
            }
        }
        reservationRepository.deleteById(reservationId);
    }

    @Transactional
    public void updateReservation(Long reservationId, LocalDateTime startTime, Integer numSlots) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("reservation with id " + reservationId + " does not exist"));

        Long clientId = reservation.getClientId();
        List<Attribute> attributes = attributeRepository.findByClientId(clientId);
        Long startTimeId = -1L;
        Long numSlotsId = -1L;
        for (Attribute attribute: attributes){
            if (attribute.getLabel().equals("startTime")){
                startTimeId = attribute.getAttributeId();
            }
            else if (attribute.getLabel().equals("numSlots")){
                numSlotsId = attribute.getAttributeId();
            }
        }

        if (startTime != null && !Objects.equals(reservation.getStartTime(), startTime)) {
            // TODO: limit number of reservations per certain time slot
            reservation.setStartTime(startTime);
            reservationRepository.updateStartTime(reservationId, startTime);
            datetimeTypeRepository.updateField(reservationId, startTimeId, startTime);
        }

        if (numSlots != null && !Objects.equals(reservation.getNumSlots(), numSlots)) {
            reservation.setNumSlots(numSlots);
            reservationRepository.updateNumSlots(reservationId, numSlots);
            integerTypeRepository.updateField(reservationId, numSlotsId, numSlots);
        }
    }
}
