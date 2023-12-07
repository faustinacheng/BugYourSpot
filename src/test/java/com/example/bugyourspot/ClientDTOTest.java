package com.example.bugyourspot;
import com.example.bugyourspot.reservation.*;


import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClientDTOTest {

    @Test
    public void testDefaultConstructor() {
        ClientDTO clientDTO = new ClientDTO();

        assertNull(clientDTO.getCustomValues());
        assertNull(clientDTO.getStartTime());
        assertNull(clientDTO.getEndTime());
        assertEquals(0, clientDTO.getSlotLength());
        assertEquals(0, clientDTO.getReservationsPerSlot());
    }

    @Test
    public void testParameterizedConstructor() {
        Map<String, String> customValues = new HashMap<>();
        customValues.put("key1", "value1");
        customValues.put("key2", "value2");
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        int slotLength = 30;
        int reservationsPerSlot = 2;

        ClientDTO clientDTO = new ClientDTO(customValues, startTime, endTime, slotLength, reservationsPerSlot);

        assertEquals(customValues, clientDTO.getCustomValues());
        assertEquals(startTime, clientDTO.getStartTime());
        assertEquals(endTime, clientDTO.getEndTime());
        assertEquals(slotLength, clientDTO.getSlotLength());
        assertEquals(reservationsPerSlot, clientDTO.getReservationsPerSlot());
    }

    @Test
    public void testSettersAndGetters() {
        ClientDTO clientDTO = new ClientDTO();

        Map<String, String> customValues = new HashMap<>();
        customValues.put("key1", "value1");
        customValues.put("key2", "value2");
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        int slotLength = 30;
        int reservationsPerSlot = 2;

        clientDTO.setCustomValues(customValues);
        clientDTO.setStartTime(startTime);
        clientDTO.setEndTime(endTime);
        clientDTO.setSlotLength(slotLength);
        clientDTO.setReservationsPerSlot(reservationsPerSlot);

        assertEquals(customValues, clientDTO.getCustomValues());
        assertEquals(startTime, clientDTO.getStartTime());
        assertEquals(endTime, clientDTO.getEndTime());
        assertEquals(slotLength, clientDTO.getSlotLength());
        assertEquals(reservationsPerSlot, clientDTO.getReservationsPerSlot());
    }
}