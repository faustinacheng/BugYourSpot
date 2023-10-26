package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.BooleanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooleanTypeRepository extends JpaRepository<BooleanType, Long> {
    BooleanType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);
}

