package com.example.bugyourspot;
import com.example.bugyourspot.reservation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConcurrentClientTest {

    @Autowired
    private ReservationRepository reservationRepository;
    private ReservationDTO reservationDTO;
    @Autowired
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

    //private Reservation reservation;
    @Mock
    private Client client;
    private final Long clientId = 1L;
    private final Long customerId = 1L;
    private final Long reservationId = 0L;
    private final Long fakeId = 2L;
    private final Long realId = 1L;
    private final int numSlots = 2;
    private final Map<String, String> customValues = new HashMap<>();
    private ClientDTO firstClientDTO;
    private ClientDTO secondClientDTO;

    //private final LocalDateTime startTime = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        HashMap<String, String> schema = new HashMap<>();
        schema.put("key", "value");

        MockitoAnnotations.openMocks(this);
        reservationService = new ReservationService(reservationRepository, clientRepository,
                attributeRepository, varcharTypeRepository, datetimeTypeRepository,  doubleTypeRepository,
                integerTypeRepository, booleanTypeRepository);

        LocalTime startTime = LocalTime.of(6, 0, 0);
        LocalTime endTime = LocalTime.of(18, 0, 0);
        firstClientDTO = new ClientDTO(schema, startTime, endTime, 1, 1);
        secondClientDTO = new ClientDTO(schema, startTime, endTime, 1, 1);
    }

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateMultipleClients() {
        Long firstClientId = reservationService.createClient(firstClientDTO);
        Optional<Client> foundFirstClient = clientRepository.findById(firstClientId);
        Long secondClientId = reservationService.createClient(secondClientDTO);
        Optional<Client> foundSecondClient = clientRepository.findById(secondClientId);

        assertTrue(foundFirstClient.isPresent());
        assertTrue(foundSecondClient.isPresent());
    }

    @Test
    public void createOneReservation() {
        Long firstClientId = reservationService.createClient(firstClientDTO);
        Long secondClientId = reservationService.createClient(secondClientDTO);

        LocalDateTime startTime = LocalDateTime.of(2023, 11, 30, 12, 0, 0);
        ReservationDTO firstReservationDTO = new ReservationDTO(firstClientId, customerId, startTime, numSlots, customValues);
        reservationService.createReservation(firstReservationDTO);

        // second client should still have 0 reservations
        List<Reservation> firstReservations = reservationRepository.findByClientId(firstClientId);
        assertEquals(firstReservations.size(), 1);
        List<Reservation> secondReservations = reservationRepository.findByClientId(secondClientId);
        assertEquals(secondReservations.size(), 0);
    }

    @Test
    public void deleteOneReservation() {
        Long firstClientId = reservationService.createClient(firstClientDTO);
        Long secondClientId = reservationService.createClient(secondClientDTO);

        LocalDateTime startTime = LocalDateTime.of(2023, 11, 30, 12, 0, 0);
        ReservationDTO firstReservationDTO = new ReservationDTO(firstClientId, customerId, startTime, numSlots, customValues);
        ReservationDTO secondReservationDTO = new ReservationDTO(secondClientId, customerId, startTime, numSlots, customValues);
        reservationService.createReservation(firstReservationDTO);
        reservationService.createReservation(secondReservationDTO);

        List<Reservation> reservations = reservationRepository.findByClientId(firstClientId);
        assertEquals(reservations.size(), 1);
        Reservation modifiedReservation =  reservations.get(0);
        Long reservationId = modifiedReservation.getReservationId();
        reservationService.deleteReservation(reservationId);

        // should not affect the second client's existing reservation
        List<Reservation> firstReservations = reservationRepository.findByClientId(firstClientId);
        assertEquals(firstReservations.size(), 0);
        List<Reservation> secondReservations = reservationRepository.findByClientId(secondClientId);
        assertEquals(secondReservations.size(), 1);
    }

    @Test
    public void updateOneReservation() {
        Long firstClientId = reservationService.createClient(firstClientDTO);
        Long secondClientId = reservationService.createClient(secondClientDTO);

        LocalDateTime startTime = LocalDateTime.of(2023, 11, 30, 12, 0, 0);
        ReservationDTO firstReservationDTO = new ReservationDTO(firstClientId, customerId, startTime, numSlots, customValues);
        ReservationDTO secondReservationDTO = new ReservationDTO(secondClientId, customerId, startTime, numSlots, customValues);
        reservationService.createReservation(firstReservationDTO);
        reservationService.createReservation(secondReservationDTO);

        List<Reservation> reservations = reservationRepository.findByClientId(firstClientId);
        assertEquals(reservations.size(), 1);
        Reservation modifiedReservation =  reservations.get(0);
        Long reservationId = modifiedReservation.getReservationId();
        reservationService.updateReservation(reservationId, modifiedReservation.getStartTime(), 4);

        // should not affect the numSlots value for second client's existing reservation
        List<Reservation> firstReservations = reservationRepository.findByClientId(firstClientId);
        assertEquals(firstReservations.size(), 1);
        List<Reservation> secondReservations = reservationRepository.findByClientId(secondClientId);
        assertEquals(secondReservations.size(), 1);

        Reservation firstReservation = firstReservations.get(0);
        Reservation secondReservation = secondReservations.get(0);
        assertEquals(firstReservation.getNumSlots(), 4);
        assertEquals(secondReservation.getNumSlots(), numSlots);
    }

}
