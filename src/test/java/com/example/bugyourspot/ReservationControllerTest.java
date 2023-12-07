package com.example.bugyourspot;
import com.example.bugyourspot.reservation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationService reservationService;
    private Reservation reservation;
    private Long reservationId = 1L;
    private Long clientId = 1L;
    private Long customerId = 1L;
    private Integer numSlots = 3;
    private LocalDateTime startTime = LocalDateTime.now();
    private Map<String, String> customValues = new HashMap<>();

    @BeforeEach
    public void setUp() {
        reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
    }

    @Test
    public void createReservation() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO(clientId, customerId, startTime, numSlots, customValues);
        //doNothing().when(reservationService).createReservation(any(ReservationDTO.class));

        // actually add reservation
        mockMvc.perform(post("/api/v1/reservation")
                        .contentType("application/json")
                        .content("{ \"id\": 1, \"startTime\": \"2023-10-17T10:00:00\", \"endTime\": \"2023-10-17T12:00:00\" }"))
                .andExpect(status().isOk());

        // verify that reservation was added
        verify(reservationService).createReservation(any(ReservationDTO.class));
    }

    @Test
    public void cancelReservation() throws Exception {
        doNothing().when(reservationService).deleteReservation(reservationId);

        // actually delete reservation
        mockMvc.perform(delete("/api/v1/reservation?reservationId=" + reservationId))
                .andExpect(status().isOk());

        // verify that reservation was deleted
        verify(reservationService).deleteReservation(reservationId);
    }

    @Test
    public void updateReservation() throws Exception {
        // actually update reservation
        mockMvc.perform(put("/api/v1/reservation")
                .contentType("application/json")
                .content("{\"reservationId\": 1,\"updateValues\": {\"startTime\": \"2023-11-29T11:00:00\",\"numSlots\": 1,\"doctorId\": 22,\"patientNotes\": \"diabetes\"}}"))
                .andExpect(status().isOk());

        // verify that reservation was updated
        ArgumentCaptor<UpdateDTO> argument = ArgumentCaptor.forClass(UpdateDTO.class);
        verify(reservationService).updateReservation(argument.capture());
        assertEquals(1, argument.getValue().getReservationId());
        assertEquals("2023-11-29T11:00:00", argument.getValue().getUpdateValues().get("startTime"));
        assertEquals("1", argument.getValue().getUpdateValues().get("numSlots"));
        assertEquals("22", argument.getValue().getUpdateValues().get("doctorId"));
        assertEquals("diabetes", argument.getValue().getUpdateValues().get("patientNotes"));

    }
}
