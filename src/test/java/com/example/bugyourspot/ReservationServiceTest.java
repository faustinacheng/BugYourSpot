package com.example.bugyourspot;

import com.example.bugyourspot.reservation.*;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.Mock;
 import org.mockito.MockitoAnnotations;
 import static org.mockito.Mockito.*;
 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.ArgumentMatchers.anyLong;
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
//     @Mock
//     private DoubleType doubleType;
//     @Mock
//     private DatetimeType datetimeType;
//     @Mock
//     private VarcharType varcharType;
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
         Client client = new Client();
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
         when(booleanType.getValue()).thenReturn(true);

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

     /** @Test
     public void updateReservation() {
         reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
         attributeRepository = Mockito.mock(AttributeRepository.class);
         datetimeTypeRepository = Mockito.mock(DatetimeTypeRepository.class);

         when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
         when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<>());

         //doNothing().when(reservationRepository).updateStartTime(reservationId, startTime);
         //doNothing().when(datetimeTypeRepository).updateField(reservationId, anyLong(), startTime);

         reservationService.updateReservation(reservationId, startTime, numSlots);
         verify(reservationRepository).updateStartTime(reservationId, startTime);
         verify(datetimeTypeRepository).updateField(reservationId, anyLong(), startTime);
     }*/

     @Test
     public void updateNonExistentReservation() {
         when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

         assertThrows(IllegalStateException.class, () -> reservationService.updateReservation(1L,
                                                             LocalDateTime.now(), numSlots));
     }

    @Test
    public void testUpdateReservation_withStartTimeChange() {
        Long reservationId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        Reservation reservation = new Reservation();
        reservation.setStartTime(startTime.plusHours(1)); // Different start time
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        reservationService.updateReservation(reservationId, startTime, null);
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

        reservationService.updateReservation(reservationId, null, numSlots);
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

        reservationService.updateReservation(reservationId, startTime, numSlots);
        verify(reservationRepository, never()).updateStartTime(anyLong(), any());
        verify(reservationRepository, never()).updateNumSlots(anyLong(), anyInt());
        verify(datetimeTypeRepository, never()).updateField(anyLong(), anyLong(), any());
        verify(integerTypeRepository, never()).updateField(anyLong(), anyLong(), anyInt());
    }
 }
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//public class ReservationServiceTest {
//
//    @Mock
//    private ReservationRepository reservationRepository;
//    private ReservationDTO reservationDTO;
//    @Mock
//    private ClientRepository clientRepository;
//    @Mock
//    private AttributeRepository attributeRepository;
//    @Mock
//    private VarcharTypeRepository varcharTypeRepository;
//    @Mock
//    private DatetimeTypeRepository datetimeTypeRepository;
//    @Mock
//    private DoubleTypeRepository doubleTypeRepository;
//    @Mock
//    private IntegerTypeRepository integerTypeRepository;
//    @Mock
//    private BooleanTypeRepository booleanTypeRepository;
//
//     private Reservation reservation;
//     private final Long clientId = 1L;
//     private final Long customerId = 1L;
//     private final Long reservationId = 0L;
//     private final Long fakeId = 2L;
//     private final Long realId = 1L;
//     private final int numSlots = 2;
//     private final Map<String, String> customValues = new HashMap<>();
//
//     private final LocalDateTime startTime = LocalDateTime.now();
//
//    private ReservationService reservationService;
//
//    @BeforeEach
//    public void setUp() {
//        // MockitoAnnotations.initMocks(this);
//        reservationService = new ReservationService(reservationRepository, clientRepository,
//                attributeRepository, varcharTypeRepository, datetimeTypeRepository, doubleTypeRepository,
//                integerTypeRepository, booleanTypeRepository);
//        reservationDTO = new ReservationDTO(clientId, customerId, startTime, numSlots, customValues);
//        reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
//    }
//
//     @Test
//     public void getReservations() {
//         when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));
//
//         List<Reservation> result = reservationService.getReservations();
//         //of all reservations, it should be first idx
//         assertEquals(reservation, result.get(0));
//     }
//
//    @Test
//    public void testGetReservationsInvalidClient() {
//        //Reservation reservation = new Reservation();
//        when(reservationRepository.findByClientId(anyLong())).thenReturn(new ArrayList<Reservation>());
//        when(attributeRepository.findByClientId(anyLong())).thenReturn(new ArrayList<Attribute>());
//
//        List<Reservation> result = reservationService.getReservations();
//
//        assertEquals(0, result.size());
//    }
//
//    @Test
//    public void testCreateClient() {
//        // Arrange
//        ClientDTO clientDTO = new ClientDTO();
//        clientDTO.setStartTime(LocalTime.now());
//        clientDTO.setEndTime(LocalTime.now().plusHours(1));
//        clientDTO.setSlotLength(30);
//        clientDTO.setReservationsPerSlot(2);
//
//        Client client = new Client();
//        when(clientRepository.save(any(Client.class))).thenReturn(client);
//        when(clientDTO.getCustomValues()).thenReturn(new HashMap<>());
//        // Act
//        reservationService.createClient(clientDTO);
//
//        // Assert
//        verify(clientRepository).save(client);
//        //verify(attributeRepository, times(clientDTO.getCustomValues().size())).save(any(Attribute.class));
//    }
//
//    @Test
//    public void testGetClients() {
//        // Arrange
//        List<Client> clients = Arrays.asList(new Client(), new Client());
//        when(clientRepository.findAll()).thenReturn(clients);
//
//        // Act
//        List<Client> result = reservationService.getClients();
//
//        // Assert
//        assertEquals(clients.size(), result.size());
//        assertEquals(clients, result);
//    }
//
//    @Test
//    public void testGetClientReservations() {
//        // Arrange
//        Long clientId = 1L;
//        Reservation reservation = new Reservation();
//        List<Reservation> reservations = Collections.singletonList(reservation);
//        when(reservationRepository.findByClientId(clientId)).thenReturn(reservations);
//
//        Attribute attribute = new Attribute();
//        List<Attribute> attributes = Collections.singletonList(attribute);
//        when(attributeRepository.findByClientId(clientId)).thenReturn(attributes);
//
//        when(datetimeTypeRepository.findByReservationIdAndAttributeId(anyLong(), anyLong())).thenReturn(new DatetimeType());
//        when(varcharTypeRepository.findByReservationIdAndAttributeId(anyLong(), anyLong())).thenReturn(new VarcharType());
//        when(integerTypeRepository.findByReservationIdAndAttributeId(anyLong(), anyLong())).thenReturn(new IntegerType());
//        when(booleanTypeRepository.findByReservationIdAndAttributeId(anyLong(), anyLong())).thenReturn(new BooleanType());
//        when(doubleTypeRepository.findByReservationIdAndAttributeId(anyLong(), anyLong())).thenReturn(new DoubleType());
//
//        // Act
//        List<Map<String, String>> result = reservationService.getClientReservations(clientId);
//
//        // Assert
//        assertEquals(1, result.size());
//        Map<String, String> entry = result.get(0);
//        assertNotNull(entry.get("reservationId"));
//        assertNotNull(entry.get("startTime"));
//        assertNotNull(entry.get("numSlots"));
//        assertNotNull(entry.get("clientId"));
//        assertNotNull(entry.get("userId"));
//    }
//}