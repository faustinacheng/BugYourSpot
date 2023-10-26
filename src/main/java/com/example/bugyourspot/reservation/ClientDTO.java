package com.example.bugyourspot.reservation;

import java.util.Map;

public class ClientDTO {

    private Map<String, String> customValues;

    public ClientDTO() {
    }

    public ClientDTO(Map<String, String> customValues) {
        this.customValues = customValues;
    }

    public Map<String, String> getCustomValues() {
        return customValues;
    }

    public void setCustomValues(Map<String, String> customValues) {
        this.customValues = customValues;
    }

}
