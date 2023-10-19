package com.example.bugyourspot;

import com.example.bugyourspot.reservation.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;

@SpringBootTest
public class ReservationTest {

    private final int clientId = 1;
    private final int customerId = 1;
    private final LocalDateTime startTime = LocalDateTime.now();
    private final LocalDateTime endTime = LocalDateTime.now().plusHours(2);
    @Test
    public void reservationProperties() {
        Reservation reservation = new Reservation(clientId, customerId, startTime, endTime);

        // basic test to check attribute retrieval and constructor initialization
        assertEquals(clientId, reservation.getClientId());
        assertEquals(customerId, reservation.getCustomerId());
        assertEquals(startTime, reservation.getStartTime());
        assertEquals(endTime, reservation.getEndTime());
    }

    @Test
    public void updateMethods() {
        Reservation reservation = new Reservation();
        // basic test to check setting attributes
        reservation.setClientId(clientId);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        assertEquals(clientId, reservation.getClientId());
        assertEquals(startTime, reservation.getStartTime());
        assertEquals(endTime, reservation.getEndTime());
    }

    @Test
    public void toStringMethod() {
        Reservation reservation = new Reservation(clientId, customerId, startTime, endTime);

        // basic test to check correctness of reservation formatting
        String expected = "Reservation{clientId=1, startTime=" + startTime + ", endTime=" + endTime + "}";
        assertEquals(expected, reservation.toString());
    }
}
