package com.example.bugyourspot;
import com.example.bugyourspot.reservation.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReservationDTOTest {

    @Test
    public void testDefaultConstructor() {
        ReservationDTO reservationDTO = new ReservationDTO(null, null, null, null, null);

        assertNull(reservationDTO.getReservationId());
        assertNull(reservationDTO.getClientId());
        assertNull(reservationDTO.getUserId());
        assertNull(reservationDTO.getStartTime());
        assertNull(reservationDTO.getNumSlots());
        assertNull(reservationDTO.getCustomValues());
    }

    @Test
    public void testParameterizedConstructor() {
        Long clientId = 1L;
        Long userId = 2L;
        LocalDateTime startTime = LocalDateTime.now();
        Integer numSlots = 3;
        Map<String, String> customValues = new HashMap<>();
        customValues.put("key1", "value1");
        customValues.put("key2", "value2");

        ReservationDTO reservationDTO = new ReservationDTO(clientId, userId, startTime, numSlots, customValues);

        assertNull(reservationDTO.getReservationId());
        assertEquals(clientId, reservationDTO.getClientId());
        assertEquals(userId, reservationDTO.getUserId());
        assertEquals(startTime, reservationDTO.getStartTime());
        assertEquals(numSlots, reservationDTO.getNumSlots());
        assertEquals(customValues, reservationDTO.getCustomValues());
    }

    @Test
    public void testSettersAndGetters() {
        ReservationDTO reservationDTO = new ReservationDTO(null, null, null, null, null);

        Long reservationId = 1L;
        Long clientId = 2L;
        Long userId = 3L;
        LocalDateTime startTime = LocalDateTime.now();
        Integer numSlots = 4;
        Map<String, String> customValues = new HashMap<>();
        customValues.put("key1", "value1");
        customValues.put("key2", "value2");

        reservationDTO.setReservationId(reservationId);
        reservationDTO.setClientId(clientId);
        reservationDTO.setUserId(userId);
        reservationDTO.setStartTime(startTime);
        reservationDTO.setNumSlots(numSlots);
        reservationDTO.setCustomValues(customValues);

        assertEquals(reservationId, reservationDTO.getReservationId());
        assertEquals(clientId, reservationDTO.getClientId());
        assertEquals(userId, reservationDTO.getUserId());
        assertEquals(startTime, reservationDTO.getStartTime());
        assertEquals(numSlots, reservationDTO.getNumSlots());
        assertEquals(customValues, reservationDTO.getCustomValues());
    }
}
