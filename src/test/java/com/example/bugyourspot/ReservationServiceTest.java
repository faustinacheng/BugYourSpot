package com.example.bugyourspot;

import com.example.bugyourspot.reservation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ReservationServiceTest {

    private ReservationService reservationService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private DatetimeTypeRepository datetimeTypeRepository;
    @Mock
    private VarcharTypeRepository varcharTypeRepository;
    @Mock
    private IntegerTypeRepository integerTypeRepository;
    @Mock
    private BooleanTypeRepository booleanTypeRepository;
    @Mock
    private DoubleTypeRepository doubleTypeRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        reservationService = new ReservationService(
            reservationRepository,
            clientRepository,
            attributeRepository,
            varcharTypeRepository,
            datetimeTypeRepository,
            doubleTypeRepository,
            integerTypeRepository,
            booleanTypeRepository);
    }

    @Test
    public void createReservationInvalidTimeMultipleTest() {
        LocalDateTime startTime = LocalDateTime.of(2023, 11, 29, 14, 15, 0);
        Map<String, String> customValues = new HashMap<String, String>();
        customValues.put("partySize", "4");
        customValues.put("birthday", "true");
        customValues.put("someTime", "2023-11-29T14:00:00");
        customValues.put("name", "John Doe");
        customValues.put("price", "12.34");

        ReservationDTO reservationDTO = new ReservationDTO(1L, 2L, startTime, 3, customValues);
        Client client = mock(Client.class);

        when(clientRepository.findReservationSchemaByClientId(anyLong())).thenReturn(client);
        when(client.getStartTime()).thenReturn(LocalTime.of(9, 0, 0));
        when(client.getEndTime()).thenReturn(LocalTime.of(21, 0, 0));
        when(client.getSlotLength()).thenReturn(30);
        when(client.getReservationsPerSlot()).thenReturn(2);

        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
    }

    @Test
    public void createReservationInvalidStartTimeTest() {
        LocalDateTime startTime = LocalDateTime.of(2023, 11, 29, 1, 0, 0);
        Map<String, String> customValues = new HashMap<String, String>();
        customValues.put("partySize", "4");
        customValues.put("birthday", "true");
        customValues.put("someTime", "2023-11-29T14:00:00");
        customValues.put("name", "John Doe");
        customValues.put("price", "12.34");

        ReservationDTO reservationDTO = new ReservationDTO(1L, 2L, startTime, 3, customValues);
        Client client = mock(Client.class);

        when(clientRepository.findReservationSchemaByClientId(anyLong())).thenReturn(client);
        when(client.getStartTime()).thenReturn(LocalTime.of(9, 0, 0));
        when(client.getEndTime()).thenReturn(LocalTime.of(21, 0, 0));
        when(client.getSlotLength()).thenReturn(30);
        when(client.getReservationsPerSlot()).thenReturn(2);

        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
    }
    
    @Test
    public void createReservationInvalidAttributeTest() {
        LocalDateTime startTime = LocalDateTime.of(2023, 11, 29, 14, 0, 0);
        Map<String, String> customValues = new HashMap<String, String>();
        customValues.put("partySize", "4");
        customValues.put("birthday", "true");
        customValues.put("someTime", "2023-11-29T14:00:00");
        customValues.put("name", "John Doe");

        ReservationDTO reservationDTO = new ReservationDTO(1L, 2L, startTime, 3, customValues);
        Client client = mock(Client.class);

        when(clientRepository.findReservationSchemaByClientId(anyLong())).thenReturn(client);
        when(client.getStartTime()).thenReturn(LocalTime.of(9, 0, 0));
        when(client.getEndTime()).thenReturn(LocalTime.of(21, 0, 0));
        when(client.getSlotLength()).thenReturn(30);
        when(client.getReservationsPerSlot()).thenReturn(2);

        when(reservationRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(null);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute(1L, "partySize", "INTEGER"));
        attributes.add(new Attribute(1L, "birthday", "BOOLEAN"));
        attributes.add(new Attribute(1L, "someTime", "DATETIME"));
        attributes.add(new Attribute(1L, "name", "VARCHAR"));
        attributes.add(new Attribute(1L, "price", "DOUBLE"));

        when(attributeRepository.findByClientId(anyLong())).thenReturn(attributes);

        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
    }
    
    @Test
    public void createReservationValidTest() {
        LocalDateTime startTime = LocalDateTime.of(2023, 11, 29, 14, 0, 0);
        Map<String, String> customValues = new HashMap<String, String>();
        customValues.put("partySize", "4");
        customValues.put("birthday", "true");
        customValues.put("someTime", "2023-11-29T14:00:00");
        customValues.put("name", "John Doe");
        customValues.put("price", "12.34");

        ReservationDTO reservationDTO = new ReservationDTO(1L, 2L, startTime, 3, customValues);
        Client client = mock(Client.class);

        when(clientRepository.findReservationSchemaByClientId(anyLong())).thenReturn(client);
        when(client.getStartTime()).thenReturn(LocalTime.of(9, 0, 0));
        when(client.getEndTime()).thenReturn(LocalTime.of(21, 0, 0));
        when(client.getSlotLength()).thenReturn(30);
        when(client.getReservationsPerSlot()).thenReturn(2);

        when(reservationRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(null);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute(1L, "partySize", "INTEGER"));
        attributes.add(new Attribute(1L, "birthday", "BOOLEAN"));
        attributes.add(new Attribute(1L, "someTime", "DATETIME"));
        attributes.add(new Attribute(1L, "name", "VARCHAR"));
        attributes.add(new Attribute(1L, "price", "DOUBLE"));

        when(attributeRepository.findByClientId(anyLong())).thenReturn(attributes);
        when(datetimeTypeRepository.save(any(DatetimeType.class))).thenReturn(null);
        when(varcharTypeRepository.save(any(VarcharType.class))).thenReturn(null);
        when(integerTypeRepository.save(any(IntegerType.class))).thenReturn(null);
        when(booleanTypeRepository.save(any(BooleanType.class))).thenReturn(null);
        when(doubleTypeRepository.save(any(DoubleType.class))).thenReturn(null);

        reservationService.createReservation(reservationDTO);
    }

    @Test
    public void createReservationFullyBookedTest() {
        LocalDateTime startTime = LocalDateTime.of(2023, 11, 29, 14, 0, 0);
        Map<String, String> customValues = new HashMap<String, String>();
        customValues.put("partySize", "4");
        customValues.put("birthday", "true");
        customValues.put("someTime", "2023-11-29T14:00:00");
        customValues.put("name", "John Doe");
        customValues.put("price", "12.34");

        ReservationDTO reservationDTO = new ReservationDTO(1L, 2L, startTime, 3, customValues);
        Client client = mock(Client.class);

        when(clientRepository.findReservationSchemaByClientId(anyLong())).thenReturn(client);
        when(client.getStartTime()).thenReturn(LocalTime.of(9, 0, 0));
        when(client.getEndTime()).thenReturn(LocalTime.of(21, 0, 0));
        when(client.getSlotLength()).thenReturn(30);
        when(client.getReservationsPerSlot()).thenReturn(2);

        List<Reservation> prevReservations = new ArrayList<>();
        prevReservations.add(new Reservation(2L, 1L, 2L, LocalDateTime.of(2023, 11, 29, 14, 0, 0), 3));
        prevReservations.add(new Reservation(3L, 1L, 3L, LocalDateTime.of(2023, 11, 29, 14, 0, 0), 3));
        when(reservationRepository.findByClientId(anyLong())).thenReturn(prevReservations);

        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
    }
}