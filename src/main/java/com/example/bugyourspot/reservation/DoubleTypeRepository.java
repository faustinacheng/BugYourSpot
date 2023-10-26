package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.DoubleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoubleTypeRepository extends JpaRepository<DoubleType, Long> {
    DoubleType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);
}

