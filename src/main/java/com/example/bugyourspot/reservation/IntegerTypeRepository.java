package com.example.bugyourspot.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IntegerTypeRepository extends JpaRepository<IntegerType, Long> {

    IntegerType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);

    @Modifying
    @Query("UPDATE IntegerType i SET i.value = ?3 WHERE i.reservationId = ?1 AND " +
            "i.attributeId = ?2")
    void updateField(Long reservationId, Long attributeId, Integer newValue);
}

