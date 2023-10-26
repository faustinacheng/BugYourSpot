package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.VarcharType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VarcharTypeRepository extends JpaRepository<VarcharType, Long> {
    VarcharType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);
}

