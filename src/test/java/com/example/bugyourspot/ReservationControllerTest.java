package com.example.bugyourspot;
import com.example.bugyourspot.reservation.Reservation;
import com.example.bugyourspot.reservation.ReservationController;
import com.example.bugyourspot.reservation.ReservationService;
import com.example.bugyourspot.reservation.ReservationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
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
    public void getReservations() throws Exception {
        // simulate client accessing this endpoint
        mockMvc.perform(get("/api/v1/reservation"));
        verify(reservationService).getReservations();
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
        mockMvc.perform(delete("/api/v1/reservation/{id}", reservationId))
                .andExpect(status().isOk());

        // verify that reservation was deleted
        verify(reservationService).deleteReservation(reservationId);
    }

    @Test
    public void updateReservation() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        doNothing().when(reservationService).updateReservation(reservationId, startTime, numSlots);

        // actually update reservation
        mockMvc.perform(put("/api/v1/reservation/{id}", reservationId)
                        .param("startTime", startTime.toString())
                        .param("numSlots", numSlots + ""))
                .andExpect(status().isOk());

        // verify that reservation was updated
        verify(reservationService).updateReservation(reservationId, startTime, 3);
    }
}
