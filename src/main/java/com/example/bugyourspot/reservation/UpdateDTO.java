package com.example.bugyourspot.reservation;

import java.util.Map;

public class UpdateDTO {
    private Long reservationId;
    private Map<String, String> updateValues;

    public UpdateDTO(Long reservationId, Map<String, String> updateValues) {
        this.reservationId = reservationId;
        this.updateValues = updateValues;
    }

    public Long getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Map<String, String> getUpdateValues() {
        return updateValues;
    }

    public void setUpdateValues(Map<String, String> updateValues) {
        this.updateValues = updateValues;
    }
}
