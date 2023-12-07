package com.example.bugyourspot;
import com.example.bugyourspot.reservation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
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

    // @Test
    // public void updateReservation() throws Exception {
    //     LocalDateTime startTime = LocalDateTime.now().plusHours(1);
    //     Map<String, String> updateValues = new HashMap<>();
    //     updateValues.put("startTime", startTime.toString());
    //     updateValues.put("numSlots", Integer.toString(numSlots));
    //     UpdateDTO updateDTO = new UpdateDTO(reservationId, updateValues);
    //     doNothing().when(reservationService).updateReservation(updateDTO);



    //     // actually update reservation
    //     mockMvc.perform(put("/api/v1/reservation")
    //                     .contentType("application/json")
    //                     .content("{'reservationId': 1," +
    //                                 "'updateValues': {" +
    //                                     "'startTime': " + startTime.toString() + "," +
    //                                     "'numSlots': " + Integer.toString(numSlots) +
    //                                 "}" + 
    //                             "}"))
    //                     .andExpect(status().isOk());

    //     // verify that reservation was updated
    //     Map<String, String> updateValues2 = new HashMap<>();
    //     updateValues2.put("startTime", startTime.toString());
    //     updateValues2.put("numSlots", Integer.toString(3));
    //     UpdateDTO updateDTO2 = new UpdateDTO(reservationId, updateValues2);
    //     verify(reservationService).updateReservation(updateDTO2);
    // }
}
