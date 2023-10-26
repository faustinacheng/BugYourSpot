package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.DatetimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DatetimeTypeRepository extends JpaRepository<DatetimeType, Long> {
    DatetimeType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);
}

