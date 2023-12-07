package com.example.bugyourspot;
import com.example.bugyourspot.reservation.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

public class ReservationTest {

    private final Long reservationId = 1L;
    private final Long clientId = 1L;
    private final Long userId = 1L;
    private final int numSlots = 3;
    private final LocalDateTime startTime = LocalDateTime.now();

    @Test
    public void defaultConstructor() {
        Reservation reservation = new Reservation();

        assertNull(reservation.getReservationId());
        assertNull(reservation.getClientId());
        assertNull(reservation.getUserId());
        assertNull(reservation.getStartTime());
        assertNull(reservation.getNumSlots());
    }

    @Test
    public void parameterizedConstructorWithReservationId() {
        Reservation reservation = new Reservation(reservationId, clientId, userId, startTime, numSlots);

        assertEquals(reservationId, reservation.getReservationId());
        assertEquals(clientId, reservation.getClientId());
        assertEquals(userId, reservation.getUserId());
        assertEquals(startTime, reservation.getStartTime());
        assertEquals(numSlots, reservation.getNumSlots());
    }

    @Test
    public void parameterizedConstructorWithoutReservationId() {
        Reservation reservation = new Reservation(clientId, userId, startTime, numSlots);

        assertNull(reservation.getReservationId());
        assertEquals(clientId, reservation.getClientId());
        assertEquals(userId, reservation.getUserId());
        assertEquals(startTime, reservation.getStartTime());
        assertEquals(numSlots, reservation.getNumSlots());
    }

    @Test
    public void settersAndGetters() {
        Reservation reservation = spy(new Reservation());
        when(reservation.getReservationId()).thenReturn(1L);

        reservation.setClientId(clientId);
        reservation.setUserId(userId);
        reservation.setStartTime(startTime);
        reservation.setNumSlots(numSlots);

        assertEquals(1, reservation.getReservationId());
        assertEquals(clientId, reservation.getClientId());
        assertEquals(userId, reservation.getUserId());
        assertEquals(startTime, reservation.getStartTime());
        assertEquals(numSlots, reservation.getNumSlots());
    }

    @Test
    public void toStringMethod() {
        Reservation reservation = new Reservation(reservationId, clientId, userId, startTime, numSlots);

        String expected = "Reservation{" +
                "reservationId=" + reservationId +
                ", clientId=" + clientId +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", numSlots=" + numSlots +
                '}';
        assertEquals(expected, reservation.toString());
    }
}