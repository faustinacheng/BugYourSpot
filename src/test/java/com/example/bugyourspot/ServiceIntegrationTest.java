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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceIntegrationTest {

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

    private final LocalDateTime startTime = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationService = new ReservationService(reservationRepository, clientRepository,
                attributeRepository, varcharTypeRepository, datetimeTypeRepository,  doubleTypeRepository,
                integerTypeRepository, booleanTypeRepository);
    }

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateClient() {
        HashMap<String, String> schema = new HashMap<>();
        schema.put("key", "value");

        ClientDTO clientDTO = new ClientDTO(schema, LocalTime.now(), LocalTime.now(), 1, 1);
        Long clientId = reservationService.createClient(clientDTO);
        assertEquals(1L, clientId);
        Optional<Client> foundClient = clientRepository.findById(clientId);

        assertTrue(foundClient.isPresent());
    }

    @Test
    public void testDeleteReservation() {
        Reservation reservation = new Reservation();
        reservation = reservationRepository.save(reservation);
        Long reservationId = reservation.getReservationId();

        reservationService.deleteReservation(reservationId);

        Optional<Reservation> deletedReservation = reservationRepository.findById(reservationId);
        assertFalse(deletedReservation.isPresent());
    }

}