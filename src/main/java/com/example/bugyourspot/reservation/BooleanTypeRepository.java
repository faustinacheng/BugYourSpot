package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.BooleanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BooleanTypeRepository extends JpaRepository<BooleanType, Long> {
    BooleanType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);

    @Modifying
    @Query("UPDATE BooleanType b SET b.value = ?3 WHERE b.reservationId = ?1 AND " +
            "b.attributeId = ?2")
    void updateField(Long reservationId, Long attributeId, Boolean bool);
}

