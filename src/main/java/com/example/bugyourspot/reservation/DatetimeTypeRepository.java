package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.DatetimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DatetimeTypeRepository extends JpaRepository<DatetimeType, Long> {
    DatetimeType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);

    @Modifying
    @Query("UPDATE DatetimeType d SET d.value = ?3 WHERE d.reservationId = ?1 AND " +
            "d.attributeId = ?2")
    void updateField(Long reservationId, Long attributeId, LocalDateTime newValue);
}

