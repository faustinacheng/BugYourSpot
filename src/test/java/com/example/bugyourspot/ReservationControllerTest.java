package com.example.bugyourspot;
import com.example.bugyourspot.reservation.Reservation;
import com.example.bugyourspot.reservation.ReservationController;
import com.example.bugyourspot.reservation.ReservationService;
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
import java.util.Arrays;

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

    @BeforeEach
    public void setUp() {
        reservation = new Reservation(reservationId, clientId, customerId, LocalDateTime.now(), numSlots);
    }

    @Test
    public void getReservations() throws Exception {
        // mock getReservations() to return list containing just one reservation
        when(reservationService.getReservations()).thenReturn(Arrays.asList(reservation));

        // simulate client accessing this endpoint
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
        verify(reservationService).getReservations();
    }

    @Test
    public void createReservation() throws Exception {
        doNothing().when(reservationService).addNewReservation(reservation);

        // actually add reservation
        mockMvc.perform(post("/")
                        .contentType("application/json")
                        .content("{ \"id\": 1, \"startTime\": \"2023-10-17T10:00:00\", \"endTime\": \"2023-10-17T12:00:00\" }"))
                .andExpect(status().isOk());

        // verify that reservation was added
        verify(reservationService).addNewReservation(any(Reservation.class));
    }

    @Test
    public void cancelReservation() throws Exception {
        doNothing().when(reservationService).deleteReservation(reservationId);

        // actually delete reservation
        mockMvc.perform(delete("/{id}", reservationId))
                .andExpect(status().isOk());

        // verify that reservation was deleted
        verify(reservationService).deleteReservation(reservationId);
    }

    @Test
    public void updateReservation() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        doNothing().when(reservationService).updateReservation(reservationId, startTime, numSlots);

        // actually update reservation
        mockMvc.perform(put("/{id}", reservationId)
                        .param("startTime", startTime.toString())
                        .param("numSlots", numSlots))
                .andExpect(status().isOk());

        // verify that reservation was updated
        verify(reservationService).updateReservation(reservationId, startTime, 3);
    }
}
