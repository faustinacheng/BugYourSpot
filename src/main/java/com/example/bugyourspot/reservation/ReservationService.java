package com.example.bugyourspot.reservation;

import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Var;
import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

    // @GetMapping
    // public List<Reservation> getReservations() {
    //     return reservationRepository.findAll();
    // }

    public Long createClient(ClientDTO clientDTO) {
        Client client = new Client();
        LocalTime startTime = clientDTO.getStartTime();
        LocalTime endTime = clientDTO.getEndTime();
        int slotLength = clientDTO.getSlotLength();
        int reservationsPerSlot = clientDTO.getReservationsPerSlot();

        // check that all required fields are present
        if (startTime == null) {
            throw new IllegalArgumentException("Missing required field: startTime");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("Missing required field: endTime");
        }
        if (slotLength <= 0) {
            throw new IllegalArgumentException("Missing/invalid required field: slotLength");
        }
        if (reservationsPerSlot <= 0) {
            throw new IllegalArgumentException("Missing/invalid required field: reservationsPerSlot");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        client.setStartTime(startTime);
        client.setEndTime(endTime);
        client.setSlotLength(slotLength);
        client.setReservationsPerSlot(reservationsPerSlot);

        clientRepository.save(client);
        
        Long clientId = client.getClientId();
        Map<String, String> schema = clientDTO.getCustomValues();

        // Call AttributeRepository to add (value, type) to attributes table for each custom attribute
        for (String attributeName : schema.keySet()) {
            String attributeType = schema.get(attributeName);

            if (attributeType == null || !attributeType.matches("DATETIME|VARCHAR|INTEGER|BOOLEAN|DOUBLE")) {
                throw new IllegalArgumentException("Missing/invalid attribute type for attribute: " + attributeName);
            } 

            Attribute attribute = new Attribute(clientId, attributeName, attributeType);
            attributeRepository.save(attribute);
        }

        return clientId;
    }

    public Client getClient (Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("client with id " + clientId + " does not exist"));
    }

    public List<Map<String, String>> getClientReservations(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new IllegalArgumentException("client with id " + clientId + " does not exist");
        }

        List<Map<String, String>> results = new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findByClientId(clientId);
        List<Attribute> attributes = attributeRepository.findByClientId(clientId);

        for (Reservation reservation: reservations) {
            Long reservationId = reservation.getReservationId();
            Map<String, String> entry = new LinkedHashMap<String, String>();
            entry.put("clientId", reservation.getClientId().toString());
            entry.put("userId", reservation.getUserId().toString());
            entry.put("reservationId", reservation.getReservationId().toString());
            entry.put("startTime", reservation.getStartTime().toString());
            entry.put("numSlots", reservation.getNumSlots().toString());

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
    public void createReservation(ReservationDTO reservationDTO) throws IllegalArgumentException {
        // Check that all required fields are present and valid
        Long clientId = reservationDTO.getClientId();
        Long userId = reservationDTO.getUserId();
        LocalDateTime startTime = reservationDTO.getStartTime();
        Integer numSlots = reservationDTO.getNumSlots();
        
        if (clientId == null) {
            throw new IllegalArgumentException("Missing required field: clientId");
        }

        if (userId == null) {
            throw new IllegalArgumentException("Missing required field: userId");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("Missing required field: startTime");
        }

        if (numSlots == null) {
            throw new IllegalArgumentException("Missing required field: numSlots");
        }

        if (numSlots <= 0) {
            throw new IllegalArgumentException("numSlots must be greater than 0");
        }

        Client client = clientRepository.findReservationSchemaByClientId(clientId);
        if (client == null) {
            throw new IllegalArgumentException("client with id " + clientId + " does not exist");
        }
         
        int slotLength = client.getSlotLength();
        
        LocalDateTime endTime = startTime.plusMinutes(slotLength * numSlots);
        if (startTime.toLocalTime().isBefore(client.getStartTime()) || endTime.toLocalTime().isAfter(client.getEndTime())) {
            throw new IllegalArgumentException("Reservation time is not within client's time range");
        }

        if (startTime.toLocalTime().getMinute() % slotLength != 0) {
            throw new IllegalArgumentException("Reservation start time is not a multiple of slot length");
        }

        Map<String, String> customValues = reservationDTO.getCustomValues();
        List<Attribute> attributes = attributeRepository.findByClientId(clientId);

        // Check that all required custom attributes are present
        for (Attribute attribute : attributes) {
            String label = attribute.getLabel();
            if (!customValues.containsKey(label)) {
                throw new IllegalArgumentException("Missing custom field: " + label);
            }
        }

        // Check that the time slots are not fully booked
        Integer clientReservationsPerSlot = client.getReservationsPerSlot();

        // Track the number of reservations per requested time slot
        Map<LocalDateTime, Integer> filledReservationsPerSlot = new HashMap<>();
        for (int i = 0; i < numSlots; i++) {
            LocalDateTime slotStartTime = startTime.plusMinutes(slotLength * i);
            filledReservationsPerSlot.put(slotStartTime, 0);
        }

        for (Reservation prevReservation: reservationRepository.findByClientId(clientId)) {
            LocalDateTime prevStartTime = prevReservation.getStartTime();
            int prevNumSlots = prevReservation.getNumSlots();

            for (int i = 0; i < prevNumSlots; i++) {
                LocalDateTime slotStartTime = prevStartTime.plusMinutes(slotLength * i);

                if (filledReservationsPerSlot.containsKey(slotStartTime)) {
                    filledReservationsPerSlot.put(slotStartTime, filledReservationsPerSlot.get(slotStartTime) + 1);

                    if (filledReservationsPerSlot.get(slotStartTime) == clientReservationsPerSlot) {
                        throw new IllegalArgumentException("Reservation time slots are fully booked");
                    }
                }
            }
        }

        // Reservation is valid, save reservation and custom values
        Reservation reservation = new Reservation(clientId, userId, startTime, numSlots);
        reservationRepository.save(reservation);

        Long reservationId = reservation.getReservationId();
        for (Attribute attribute : attributes) {
            String dataType = attribute.getDataType();
            String value = customValues.get(attribute.getLabel());

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

    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findByReservationId(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException("reservation with id " + reservationId + " does not exist");
        }

        // get the attributes associated with this reservation (based on which client it belongs to)
        Long clientId = reservation.getClientId();

        List<Attribute> attributes = attributeRepository.findByClientId(clientId);
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
    public void updateReservation(UpdateDTO updateDto) {
        Long reservationId = updateDto.getReservationId();
        Reservation reservation = reservationRepository.findByReservationId(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException("reservation with id " + reservationId + " does not exist");
        }

        Long clientId = reservation.getClientId();
        List<Attribute> attributes = attributeRepository.findByClientId(clientId);
        
        // for each key-value pair in the updateDto, update the corresponding attribute if it exists
        for (Map.Entry<String, String> entry : updateDto.getUpdateValues().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.equals("startTime") || key.equals("numSlots")) {
                Client client = clientRepository.findReservationSchemaByClientId(clientId);

                LocalDateTime startTime = reservation.getStartTime();
                Integer numSlots = reservation.getNumSlots();

                if (key.equals("startTime")) {
                    startTime = LocalDateTime.parse(value);

                    if (startTime.toLocalTime().isBefore(client.getStartTime())) {
                        throw new IllegalArgumentException("Reservation time is not within client's time range");
                    }

                    if (startTime.toLocalTime().getMinute() % client.getSlotLength() != 0) {
                        throw new IllegalArgumentException("Reservation start time is not a multiple of slot length");
                    }

                } else if (key.equals("numSlots")) {
                    numSlots = Integer.parseInt(value);

                    if (numSlots <= 0) {
                        throw new IllegalArgumentException("numSlots must be greater than 0");
                    }

                    LocalDateTime endTime = reservation.getStartTime().plusMinutes(client.getSlotLength() * numSlots);

                    if (endTime.toLocalTime().isAfter(client.getEndTime())) {
                        throw new IllegalArgumentException("Reservation time is not within client's time range");
                    }
                }

                // Check that the time slots are not fully booked
                Integer clientReservationsPerSlot = client.getReservationsPerSlot();

                // Track the number of reservations per requested time slot
                Map<LocalDateTime, Integer> filledReservationsPerSlot = new HashMap<>();
                for (int i = 0; i < numSlots; i++) {
                    LocalDateTime slotStartTime = startTime.plusMinutes(client.getSlotLength() * i);
                    filledReservationsPerSlot.put(slotStartTime, 0);
                }

                for (Reservation prevReservation: reservationRepository.findByClientId(clientId)) {
                    if (prevReservation.getReservationId().equals(reservationId)) {
                        continue;
                    }

                    LocalDateTime prevStartTime = prevReservation.getStartTime();
                    int prevNumSlots = prevReservation.getNumSlots();

                    for (int i = 0; i < prevNumSlots; i++) {
                        LocalDateTime slotStartTime = prevStartTime.plusMinutes(client.getSlotLength() * i);

                        if (filledReservationsPerSlot.containsKey(slotStartTime)) {
                            filledReservationsPerSlot.put(slotStartTime, filledReservationsPerSlot.get(slotStartTime) + 1);

                            if (filledReservationsPerSlot.get(slotStartTime) == clientReservationsPerSlot) {
                                throw new IllegalArgumentException("Reservation time slots are fully booked");
                            }
                        }
                    }
                }

                if (key.equals("startTime")) {
                    reservationRepository.updateStartTime(reservationId, startTime);
                } else if (key.equals("numSlots")) {
                    reservationRepository.updateNumSlots(reservationId, numSlots);
                }
                
                continue;
            }

            // find the attribute with the given label
            Attribute attribute = null;
            for (Attribute a : attributes) {
                if (a.getLabel().equals(key)) {
                    attribute = a;
                    break;
                }
            }

            if (attribute == null) {
                throw new IllegalArgumentException("attribute with label " + key + " does not exist");
            }

            String dataType = attribute.getDataType();
            Long attributeId = attribute.getAttributeId();

            switch (dataType) {
                case "DATETIME" -> {
                    LocalDateTime datetime = LocalDateTime.parse(value);
                    datetimeTypeRepository.updateField(reservationId, attributeId, datetime);
                }
                case "VARCHAR" -> varcharTypeRepository.updateField(reservationId, attributeId, value);
                case "INTEGER" -> {
                    Integer integer = Integer.parseInt(value);
                    integerTypeRepository.updateField(reservationId, attributeId, integer);
                }
                case "BOOLEAN" -> {
                    Boolean bool = Boolean.parseBoolean(value);
                    booleanTypeRepository.updateField(reservationId, attributeId, bool);
                }
                case "DOUBLE" -> {
                    Double dbl = Double.parseDouble(value);
                    doubleTypeRepository.updateField(reservationId, attributeId, dbl);
                }
            }
        }
    }

    // @Transactional
    // public void updateReservation(Long reservationId, LocalDateTime startTime, Integer numSlots) {
    //     Reservation reservation = reservationRepository.findByReservationId(reservationId);

    //     if (reservation == null) {
    //         throw new IllegalStateException("reservation with id " + reservationId + " does not exist");
    //     }

    //     Long clientId = reservation.getClientId();
    //     List<Attribute> attributes = attributeRepository.findByClientId(clientId);
    //     Long startTimeId = -1L;
    //     Long numSlotsId = -1L;
    //     for (Attribute attribute: attributes){
    //         if (attribute.getLabel().equals("startTime")){
    //             startTimeId = attribute.getAttributeId();
    //         }
    //         else if (attribute.getLabel().equals("numSlots")){
    //             numSlotsId = attribute.getAttributeId();
    //         }
    //     }

    //     numSlotsId = 3L;

    //     if (startTime != null && !Objects.equals(reservation.getStartTime(), startTime)) {
    //         reservation.setStartTime(startTime);
    //         reservationRepository.updateStartTime(reservationId, startTime);
    //         datetimeTypeRepository.updateField(reservationId, startTimeId, startTime);
    //     }

    //     if (numSlots != null && !Objects.equals(reservation.getNumSlots(), numSlots)) {
    //         reservation.setNumSlots(numSlots);
    //         reservationRepository.updateNumSlots(reservationId, numSlots);
    //         integerTypeRepository.updateField(reservationId, numSlotsId, numSlots);
    //     }
    // }
}
