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
     private ClientDTO clientDTO = new ClientDTO();
     private final Long clientId = 1L;
     private final Long userId = 1L;
     private final Long reservationId = 1L;
     private final Long fakeId = 2L;
     private final Long realId = 1L;
     private final int numSlots = 2;
     private final Map<String, String> customValues = new HashMap<>();

     private final LocalTime clientStartTime = LocalTime.of(6, 0, 0);
     private final LocalTime clientEndTime = LocalTime.of(18, 0, 0);
     private final int slotLength = 60;
     private final int reservationsPerSlot = 2;
     private final LocalDateTime resStartTime = LocalDateTime.of(2023, 11, 29, 11, 0, 0);

     @BeforeEach
     public void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationService = new ReservationService(reservationRepository, clientRepository,
                attributeRepository, varcharTypeRepository, datetimeTypeRepository,  doubleTypeRepository,
                integerTypeRepository, booleanTypeRepository);

        client = new Client(clientStartTime, clientEndTime, slotLength, reservationsPerSlot);
        clientDTO = new ClientDTO(null, clientStartTime, clientEndTime, slotLength, reservationsPerSlot);

        reservationDTO = new ReservationDTO(clientId, userId, resStartTime, numSlots, customValues);
        reservation = new Reservation(reservationId, clientId, userId, resStartTime, 1);
     }

    //  @Test
    //  public void getReservations() {
    //      when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

    //      List<Reservation> result = reservationService.getReservations();
    //      assertEquals(reservation, result.get(0));
    //  }

    @Test
    public void createClientMissingStartTime() {
        clientDTO.setStartTime(null);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createClient(clientDTO));
    }

    @Test
    public void createClientMissingEndTime() {
        clientDTO.setEndTime(null);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createClient(clientDTO));
    }

    @Test
    public void createClientInvalidSlotLength() {
        clientDTO.setSlotLength(-1);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createClient(clientDTO));
    }

    @Test
    public void createClientNegativeReservationsPerSlot() {
        clientDTO.setReservationsPerSlot(-1);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createClient(clientDTO));
    }

    @Test
    public void createClientInvalidStartTime() {
        clientDTO.setStartTime(LocalTime.of(22, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> reservationService.createClient(clientDTO));
    }

    @Test
    public void createClientInvalidAttributeType() {
        HashMap<String, String> schema = new HashMap<>();
        schema.put("key", "INVALID");
        clientDTO.setCustomValues(schema);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createClient(clientDTO));
    }

    @Test
     public void createClient() {
         HashMap<String, String> schema = new HashMap<>();
         schema.put("key", "VARCHAR");

         ClientDTO clientDTO = new ClientDTO(schema, clientStartTime, clientEndTime, 1, 1);
         //when(clientRepository.save(any(Client.class))).thenReturn(client);
         //when(attributeRepository.save(any(Attribute.class))).thenReturn(mock(Attribute.class));
         reservationService.createClient(clientDTO);
         verify(attributeRepository).save(any(Attribute.class));
     }

    @Test
    public void getClient() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

        Client result = reservationService.getClient(clientId);
        assertEquals(client, result);
    }

    @Test
    public void getNonExistentClient() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> reservationService.getClient(fakeId));
    }

    @Test
    public void getNonExistentClientReservations(){
        when(clientRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> reservationService.getClientReservations(fakeId));
    }

     @Test
     public void getExistingClientReservations(){
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
        
         when(clientRepository.existsById(anyLong())).thenReturn(true);
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
         Reservation reservation = new Reservation(reservationId, clientId, userId, LocalDateTime.now(), numSlots);
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
        assertThrows(IllegalArgumentException.class, () -> reservationService.deleteReservation(fakeId));
    }

    @Test
    public void createReservationMissingClientId() {
        reservationDTO.setClientId(null);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
    }
    
    @Test
    public void createReservationMissingUserId() {
        reservationDTO.setUserId(null);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
    }

    @Test
    public void createReservationMissingStartTime() {
        reservationDTO.setStartTime(null);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
    }

    @Test
    public void createReservationMissingNumSlots() {
        reservationDTO.setNumSlots(null);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
    }

    @Test
    public void createReservationNegativeNumSlots() {
        reservationDTO.setNumSlots(-1);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
    }

    @Test
    public void createReservationInvalidClientId() {
        when(clientRepository.findReservationSchemaByClientId(anyLong())).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO));
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
    public void updateNonExistentReservation() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("startTime", resStartTime.toString());
        updateValues.put("numSlots", Integer.toString(numSlots));
        UpdateDTO updateDTO = new UpdateDTO(1L, updateValues);

        assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(updateDTO));
    }

    @Test
    public void testUpdateReservation_invalidStartTime() {
        Map<String, String> updateValues = new HashMap<>();
        LocalTime updatedStartTime = (clientStartTime.minusHours(1));
        LocalDateTime updatedStartDateTime = LocalDateTime.of(resStartTime.toLocalDate(), updatedStartTime);
        updateValues.put("startTime", updatedStartDateTime.toString());
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);

        assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(updateDTO));
    }

    @Test
    public void testUpdateReservation_invalidSlotMultiple() {
        Map<String, String> updateValues = new HashMap<>();
        LocalDateTime updatedStartTime = resStartTime.plusMinutes(1);
        updateValues.put("startTime", updatedStartTime.toString());
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);

        assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(updateDTO));
    }

    @Test
    public void testUpdateReservation_negativeNumSlots() {
        Map<String, String> updateValues = new HashMap<>();
        int updatedNumSlots = -1;
        updateValues.put("numSlots", Integer.toString(updatedNumSlots));
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);

        assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(updateDTO));
    }

    @Test
    public void testUpdateReservation_invalidEndTime() {
        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("numSlots", Integer.toString(10));
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);
        
        assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(updateDTO));
    }

    @Test
    public void testUpdateReservation_fullSlots() {
        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("numSlots", Integer.toString(1));
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        Reservation reservation2 = new Reservation(reservationId + 1, clientId, userId, resStartTime, 1);
        Reservation reservation3 = new Reservation(reservationId + 2, clientId, userId, resStartTime, 1);

        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);
        when(reservationRepository.findByClientId(clientId)).thenReturn(Arrays.asList(reservation2, reservation3));
        
        assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(updateDTO));
    }

    @Test
    public void testUpdateReservation_invalidAttribute() {
        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("invalid", Integer.toString(10));
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);
        
        assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(updateDTO));
    }

    @Test
    public void testUpdateReservation_withStartTimeChange() {
        Map<String, String> updateValues = new HashMap<>();
        LocalDateTime updatedStartTime = resStartTime.plusHours(1);
        updateValues.put("startTime", updatedStartTime.toString());
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);
        when(reservationRepository.findByClientId(clientId)).thenReturn(new ArrayList<>());
        
        reservationService.updateReservation(updateDTO);
        verify(reservationRepository).updateStartTime(eq(reservationId), eq(updatedStartTime));
    }

    @Test
    public void testUpdateReservation_withNumSlotsChange() {
        Map<String, String> updateValues = new HashMap<>();
        int updatedNumSlots = numSlots + 1;
        updateValues.put("numSlots", Integer.toString(updatedNumSlots));
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);
        when(reservationRepository.findByClientId(clientId)).thenReturn(new ArrayList<>());

        reservationService.updateReservation(updateDTO);
        verify(reservationRepository).updateNumSlots(eq(reservationId), eq(updatedNumSlots));
    }

    @Test
    public void testUpdateReservation_withCustomValueChange() {
        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("doctorId", Integer.toString(100));
        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        Attribute attribute = new Attribute(clientId, "doctorId", "INTEGER");

        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(Arrays.asList(attribute));
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);
        when(reservationRepository.findByClientId(clientId)).thenReturn(new ArrayList<>());
        
        reservationService.updateReservation(updateDTO);
        verify(integerTypeRepository).updateField(eq(reservationId), any(), eq(100));
    }

    @Test
    public void testUpdateReservation_withVariousAttributes() {
        Map<String, String> updateValues = new HashMap<>();
        LocalDateTime updatedStartTime = resStartTime.plusHours(1);
        updateValues.put("startTime", updatedStartTime.toString());
        updateValues.put("doctorId", Integer.toString(100));
        updateValues.put("patientNotes", "diabetes");
        updateValues.put("insurance", "true");
        updateValues.put("birthday", "1999-01-01T00:00:00");
        updateValues.put("temperature", Double.toString(98.6));

        UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);

        Attribute attribute = new Attribute(clientId, "doctorId", "INTEGER");
        Attribute attribute2 = new Attribute(clientId, "patientNotes", "VARCHAR");
        Attribute attribute3 = new Attribute(clientId, "insurance", "BOOLEAN");
        Attribute attribute4 = new Attribute(clientId, "birthday", "DATETIME");
        Attribute attribute6 = new Attribute(clientId, "temperature", "DOUBLE");
                    
        when(reservationRepository.findByReservationId(anyLong())).thenReturn(reservation);
        when(attributeRepository.findByClientId(anyLong())).thenReturn(Arrays.asList(attribute, attribute2, attribute3, attribute4, attribute6));
        when(clientRepository.findReservationSchemaByClientId(clientId)).thenReturn(client);
        when(reservationRepository.findByClientId(clientId)).thenReturn(new ArrayList<>());
        
        reservationService.updateReservation(updateDTO);
        verify(reservationRepository).updateStartTime(eq(reservationId), eq(updatedStartTime));
        verify(integerTypeRepository).updateField(eq(reservationId), any(), eq(100));
        verify(varcharTypeRepository).updateField(eq(reservationId), any(), eq("diabetes"));
    }
 }