package com.example.bugyourspot;

import com.example.bugyourspot.reservation.ReservationRepository;
import com.example.bugyourspot.reservation.ClientRepository;
import com.example.bugyourspot.reservation.AttributeRepository;
import com.example.bugyourspot.reservation.ReservationService;
import com.example.bugyourspot.reservation.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    private ClientRepository reservationSchemaRepository;
    private AttributeRepository attributeRepository;
    private ReservationService reservationService;
    private Reservation reservation;
    private final Long clientId = 1L;
    private final Long customerId = 1L;
    private final Long reservationId = 0L;
    private final Long fakeId = 2L;
    private final Long realId = 1L;
    private final int numSlots = 2;

    private final LocalDateTime startTime = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reservationService = new ReservationService(reservationRepository, reservationSchemaRepository,
                                attributeRepository);
        reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
    }

    @Test
    public void getReservations() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        List<Reservation> result = reservationService.getReservations();
        //of all reservations, it should be first idx
        assertEquals(reservation, result.get(0));
    }

    @Test
    public void addReservation() {
        when(reservationRepository.findReservationByClientId(anyInt())).thenReturn(Optional.empty());

        reservationService.addNewReservation(reservation);
        //check if saved correctly
        verify(reservationRepository).save(reservation);
    }

    @Test
    public void addClientTaken() {
        when(reservationRepository.save(any(Reservation.class))).thenThrow(new IllegalStateException());
        assertThrows(IllegalStateException.class, () -> reservationService.addNewReservation(reservation));
    }

    @Test
    public void deleteReservation() {
        //delete reservation with clientId
        reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
        when(reservationRepository.existsById(reservationId)).thenReturn(true);

        reservationService.deleteReservation(reservationId);
        verify(reservationRepository).deleteById(reservationId);
    }

    @Test
    public void deleteNonExistentReservation() {
        assertThrows(IllegalStateException.class, () -> reservationService.deleteReservation(fakeId));
    }

    @Test
    public void updateReservation() {
        reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        reservationService.updateReservation(reservationId, startTime, numSlots);

        assertEquals(startTime, reservation.getStartTime());
        assertEquals(numSlots, reservation.getNumSlots());
    }

    @Test
    public void updateNonExistentReservation() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> reservationService.updateReservation(1L,
                                                            LocalDateTime.now(), numSlots));
    }
}
