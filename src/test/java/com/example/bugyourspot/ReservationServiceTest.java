package com.example.bugyourspot;

import com.example.bugyourspot.reservation.ReservationRepository;
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
    private ReservationService reservationService;
    private Reservation reservation;
    private final int clientId = 1;
    private final int customerId = 1;
    private final long reservationId = 0;
    private final long realId = (long) 1;
    private final long fakeId = (long) 2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reservationService = new ReservationService(reservationRepository);
        reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
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
        reservationService.addNewReservation(reservation);
        assertThrows(IllegalStateException.class, () -> reservationService.addNewReservation(reservation));
    }

    @Test
    public void deleteReservation() {
        //delete reservation with clientId

        reservationService.addNewReservation(reservation);
        reservationService.deleteReservation(reservationId);

        // verify this method was eventually invoked through the layers
        verify(reservationRepository).deleteById(realId);
        // reservation should not exist anymore
        assertFalse(reservationRepository.existsById(realId));
    }

    @Test
    public void deleteNonExistentReservation() {
        assertThrows(IllegalStateException.class, () -> reservationService.deleteReservation(fakeId));
    }

    @Test
    public void updateReservation() {
        //give new start and end time
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(3);
        Reservation reservation = new Reservation(clientId, customerId, startTime, endTime);
        Long reservationId = reservation.getReservationId();
        reservationService.updateReservation(reservationId, startTime, endTime);

        assertEquals(startTime, reservation.getStartTime());
        assertEquals(endTime, reservation.getEndTime());
    }

    @Test
    public void updateNonExistentReservation() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> reservationService.updateReservation(1L, LocalDateTime.now(), LocalDateTime.now()));
    }
}
