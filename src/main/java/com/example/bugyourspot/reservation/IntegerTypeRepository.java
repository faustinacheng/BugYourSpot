package com.example.bugyourspot.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegerTypeRepository extends JpaRepository<IntegerType, Long> {

    IntegerType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);
}

