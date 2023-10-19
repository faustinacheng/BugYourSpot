package com.example.bugyourspot.reservation;

import java.time.LocalDateTime;
import java.util.Map;

public class ReservationDTO {
    private Integer clientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Map<String, String> customValues; // To hold custom fields

    // TODO: getters, setters, etc.
}
