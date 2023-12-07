package com.example.bugyourspot;
import com.example.bugyourspot.reservation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
 import java.util.List;
 import java.util.ArrayList;
 import java.util.Optional;
 import java.util.Map;
 import java.util.HashMap;

 public class ReservationServiceTest {

     @Mock
     private ReservationRepository reservationRepository;
     private ReservationDTO reservationDTO;
     @Mock
     private ClientRepository clientRepository;
     @Mock
     private AttributeRepository attributeRepository;
     private ReservationService reservationService;
     @Mock
     private VarcharTypeRepository varcharTypeRepository;
     @Mock
     private DatetimeTypeRepository datetimeTypeRepository;
     @Mock
     private DoubleTypeRepository doubleTypeRepository;
     @Mock
     private IntegerTypeRepository integerTypeRepository;
     @Mock
     private BooleanTypeRepository booleanTypeRepository;

     private Reservation reservation;
     @Mock
     private Client client;
     private final Long clientId = 1L;
     private final Long customerId = 1L;
     private final Long reservationId = 0L;
     private final Long fakeId = 2L;
     private final Long realId = 1L;
     private final int numSlots = 2;
     private final Map<String, String> customValues = new HashMap<>();

     private final LocalDateTime startTime = LocalDateTime.now();

     @BeforeEach
     public void setUp() {
         MockitoAnnotations.openMocks(this);
         reservationService = new ReservationService(reservationRepository, clientRepository,
                 attributeRepository, varcharTypeRepository, datetimeTypeRepository,  doubleTypeRepository,
                 integerTypeRepository, booleanTypeRepository);
         reservationDTO = new ReservationDTO(clientId, customerId, startTime, numSlots, customValues);
     }

     @Test
     public void getReservations() {
         when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

         List<Reservation> result = reservationService.getReservations();
         assertEquals(reservation, result.get(0));
     }

     @Test
     public void createClient(){
         HashMap<String, String> schema = new HashMap<>();
         schema.put("key", "value");

         ClientDTO clientDTO = new ClientDTO(schema, LocalTime.now(), LocalTime.now(), 1, 1);
         //when(clientRepository.save(any(Client.class))).thenReturn(client);
         //when(attributeRepository.save(any(Attribute.class))).thenReturn(mock(Attribute.class));
         Long clientID = reservationService.createClient(clientDTO);
         verify(attributeRepository).save(any(Attribute.class));
     }

     @Test
     public void getNonExistentClientReservations(){
         when(reservationRepository.findByClientId(anyLong())).thenReturn(new ArrayList<Reservation>());
         when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<Attribute>());

         List<Map<String, String>> allReservations = reservationService.getClientReservations(1L);
         assertEquals(0, allReservations.size());
     }

     @Test
     public void getExistingClientReservations(){
         Reservation reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
         ArrayList<Reservation> clientReservations = new ArrayList<>();
         clientReservations.add(reservation);

         ArrayList<Attribute> clientAttributes = new ArrayList<>();
         Attribute doubleAttribute = new Attribute(1L, "Time", "DOUBLE");
         Attribute datetimeAttribute = new Attribute(1L, "Time", "DATETIME");
         Attribute varcharAttribute = new Attribute(1L, "Time", "VARCHAR");
         Attribute integerAttribute = new Attribute(1L, "Time", "INTEGER");
         Attribute booleanAttribute = new Attribute(1L, "Time", "BOOLEAN");
         clientAttributes.add(doubleAttribute);
         clientAttributes.add(datetimeAttribute);
         clientAttributes.add(varcharAttribute);
         clientAttributes.add(integerAttribute);
         clientAttributes.add(booleanAttribute);

         DoubleType doubleType = mock(DoubleType.class);
         DatetimeType datetimeType = mock(DatetimeType.class);
         VarcharType varcharType = mock(VarcharType.class);
         IntegerType integerType = mock(IntegerType.class);
         BooleanType booleanType = mock(BooleanType.class);

         when(reservationRepository.findByClientId(anyLong())).thenReturn(clientReservations);
         when(attributeRepository.findByClientId(anyLong())).thenReturn(clientAttributes);

         when(doubleTypeRepository.findByReservationIdAndAttributeId(any(), any()))
                 .thenReturn(doubleType);
         when(doubleType.getValue()).thenReturn(2.0);
         when(datetimeTypeRepository.findByReservationIdAndAttributeId(any(), any()))
                 .thenReturn(datetimeType);
         when(datetimeType.getValue()).thenReturn(LocalDateTime.now());
         when(varcharTypeRepository.findByReservationIdAndAttributeId(any(), any()))
                 .thenReturn(varcharType);
         when(varcharType.getValue()).thenReturn("");
         when(integerTypeRepository.findByReservationIdAndAttributeId(any(), any()))
                 .thenReturn(integerType);
         when(integerType.getValue()).thenReturn(1);
         when(booleanTypeRepository.findByReservationIdAndAttributeId(any(), any()))
                 .thenReturn(booleanType);
         when(booleanType.getValue()).thenReturn(false);

         List<Map<String, String>> allReservations = reservationService.getClientReservations(1L);
         assertEquals(1, allReservations.size());
     }

     @Test
     public void deleteReservation() {
         Reservation reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
         ArrayList<Attribute> clientAttributes = new ArrayList<Attribute>();
         Attribute doubleAttribute = new Attribute(1L, "Time", "DOUBLE");
         Attribute datetimeAttribute = new Attribute(1L, "Time", "DATETIME");
         Attribute varcharAttribute = new Attribute(1L, "Time", "VARCHAR");
         Attribute integerAttribute = new Attribute(1L, "Time", "INTEGER");
         Attribute booleanAttribute = new Attribute(1L, "Time", "BOOLEAN");
         clientAttributes.add(doubleAttribute);
         clientAttributes.add(datetimeAttribute);
         clientAttributes.add(varcharAttribute);
         clientAttributes.add(integerAttribute);
         clientAttributes.add(booleanAttribute);

         when(reservationRepository.existsById(anyLong())).thenReturn(true);
         when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
         when(attributeRepository.findByClientId(anyLong())).thenReturn(clientAttributes);
         doNothing().when(datetimeTypeRepository).deleteById(anyLong());
         doNothing().when(varcharTypeRepository).deleteById(anyLong());
         doNothing().when(integerTypeRepository).deleteById(anyLong());
         doNothing().when(booleanTypeRepository).deleteById(anyLong());
         doNothing().when(doubleTypeRepository).deleteById(anyLong());

         reservationService.deleteReservation(1L);

         verify(datetimeTypeRepository).deleteById(anyLong());
         verify(varcharTypeRepository).deleteById(anyLong());
         verify(integerTypeRepository).deleteById(anyLong());
         verify(booleanTypeRepository).deleteById(anyLong());
         verify(doubleTypeRepository).deleteById(anyLong());
     }

    @Test
    public void deleteNonExistentReservation() {
        assertThrows(IllegalStateException.class, () -> reservationService.deleteReservation(fakeId));
    }

    @Test
    public void updateNonExistentReservation() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("startTime", startTime.toString());
        updateValues.put("numSlots", Integer.toString(numSlots));
        UpdateDTO updateDTO = new UpdateDTO(1L, updateValues);

        assertThrows(IllegalStateException.class, () -> reservationService.updateReservation(updateDTO));
    }

    @Test
    public void testUpdateReservation_withStartTimeChange() {
        Long reservationId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        Reservation reservation = new Reservation();
        reservation.setStartTime(startTime.plusHours(1)); // Different start time
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("startTime", startTime.toString());
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        reservationService.updateReservation(updateDTO);
        verify(reservationRepository).updateStartTime(eq(reservationId), eq(startTime));
        verify(datetimeTypeRepository).updateField(eq(reservationId), anyLong(), eq(startTime));
    }

    @Test
    public void testUpdateReservation_withNumSlotsChange() {
        Long reservationId = 1L;
        Integer numSlots = 5;
        Reservation reservation = new Reservation();
        reservation.setNumSlots(numSlots + 1); // Different numSlots
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("numSlots", Integer.toString(numSlots));
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        reservationService.updateReservation(updateDTO);
        verify(reservationRepository).updateNumSlots(eq(reservationId), eq(numSlots));
        verify(integerTypeRepository).updateField(eq(reservationId), anyLong(), eq(numSlots));
    }

    @Test
    public void testUpdateReservation_withNoChange() {
        Long reservationId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        Integer numSlots = 5;
        Reservation reservation = new Reservation();
        reservation.setStartTime(startTime);
        reservation.setNumSlots(numSlots);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("startTime", startTime.toString());
        updateValues.put("numSlots", Integer.toString(numSlots));
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        reservationService.updateReservation(updateDTO);
        verify(reservationRepository, never()).updateStartTime(anyLong(), any());
        verify(reservationRepository, never()).updateNumSlots(anyLong(), anyInt());
        verify(datetimeTypeRepository, never()).updateField(anyLong(), anyLong(), any());
        verify(integerTypeRepository, never()).updateField(anyLong(), anyLong(), anyInt());
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

    @Test
    public void testUpdateReservation_withVariousAttributes() {
        // Arrange
        Long reservationId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        Integer numSlots = 5;

        ArrayList<Attribute> clientAttributes = new ArrayList<>();
        Attribute doubleAttribute = mock(Attribute.class);
        Attribute datetimeAttribute = mock(Attribute.class);
        Attribute varcharAttribute = mock(Attribute.class);
        Attribute integerAttribute = mock(Attribute.class);
        Attribute booleanAttribute = mock(Attribute.class);
        when(doubleAttribute.getLabel()).thenReturn("Time");
        when(datetimeAttribute.getLabel()).thenReturn("startTime");
        when(varcharAttribute.getLabel()).thenReturn("Time");
        when(integerAttribute.getLabel()).thenReturn("numSlots");
        when(booleanAttribute.getLabel()).thenReturn("Time");

        clientAttributes.add(doubleAttribute);
        clientAttributes.add(datetimeAttribute);
        clientAttributes.add(varcharAttribute);
        clientAttributes.add(integerAttribute);
        clientAttributes.add(booleanAttribute);

        Reservation reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
        reservation.setStartTime(startTime);
        reservation.setNumSlots(numSlots);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(attributeRepository.findByClientId(reservation.getClientId())).thenReturn(clientAttributes);

        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("startTime", startTime.toString());
        updateValues.put("numSlots", Integer.toString(numSlots));
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        reservationService.updateReservation(updateDTO);

        verify(doubleAttribute, atLeastOnce()).getLabel();
        verify(datetimeAttribute, atLeastOnce()).getLabel();
        verify(varcharAttribute, atLeastOnce()).getLabel();
        verify(integerAttribute, atLeastOnce()).getLabel();
        verify(booleanAttribute, atLeastOnce()).getLabel();
    }
 }