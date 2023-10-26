package com.example.bugyourspot;

import com.example.bugyourspot.reservation.Reservation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;

public class ReservationTest {

    @Mock
    private final Long reservationId = 1L;
    private final Long clientId = 1L;
    private final Long userId = 1L;
    private final int numSlots = 3;
    private final LocalDateTime startTime = LocalDateTime.now();

    @Test
    public void reservationProperties() {
        Reservation reservation = new Reservation(clientId, userId, startTime, numSlots);

        // basic test to check attribute retrieval and constructor initialization
        assertEquals(clientId, reservation.getClientId());
        assertEquals(userId, reservation.getUserId());
        assertEquals(startTime, reservation.getStartTime());
        assertEquals(numSlots, reservation.getNumSlots());
    }

    @Test
    public void updateMethods() {
        Reservation reservation = new Reservation();
        // basic test to check setting attributes
        reservation.setClientId(clientId);
        reservation.setStartTime(startTime);
        reservation.setNumSlots(numSlots);

        assertEquals(clientId, reservation.getClientId());
        assertEquals(startTime, reservation.getStartTime());
        assertEquals(numSlots, reservation.getNumSlots());
    }

    @Test
    public void toStringMethod() {
        Reservation reservation = new Reservation(reservationId, clientId, userId, startTime, numSlots);

        // basic test to check correctness of reservation formatting
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
