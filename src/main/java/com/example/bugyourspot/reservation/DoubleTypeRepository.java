package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.DoubleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoubleTypeRepository extends JpaRepository<DoubleType, Long> {
    DoubleType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);

    @Modifying
    @Query("UPDATE DoubleType d SET d.value = ?3 WHERE d.reservationId = ?1 AND " +
            "d.attributeId = ?2")
    void updateField(Long reservationId, Long attributeId, Double bool);
}

