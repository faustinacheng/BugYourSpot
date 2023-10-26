package com.example.bugyourspot.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IntegerTypeRepository extends JpaRepository<IntegerType, Long> {

    IntegerType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);

    @Query("UPDATE DatetimeType d SET d.value = ?3 WHERE d.reservationId = ?1 AND " +
            "d.attributeId = ?2")
    void updateField(Long reservationId, Long attributeId, Integer newValue);
}

