package com.example.bugyourspot;

import com.example.bugyourspot.reservation.Reservation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;

public class ReservationTest {

    @Mock
    private final Long clientId = 1L;
    private final Long customerId = 1L;
    private final int numSlots = 3;
    private final LocalDateTime startTime = LocalDateTime.now();

    @Test
    public void reservationProperties() {
        Reservation reservation = new Reservation(clientId, customerId, startTime, numSlots);

        // basic test to check attribute retrieval and constructor initialization
        assertEquals(clientId, reservation.getClientId());
        assertEquals(customerId, reservation.getCustomerId());
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
        Reservation reservation = new Reservation(clientId, customerId, startTime, numSlots);

        // basic test to check correctness of reservation formatting
        String expected = "Reservation{clientId=1, startTime=" + startTime + ", numSlots=" + numSlots + "}";
        assertEquals(expected, reservation.toString());
    }
}
