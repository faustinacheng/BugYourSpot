package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.VarcharType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VarcharTypeRepository extends JpaRepository<VarcharType, Long> {
    VarcharType findByReservationIdAndAttributeId(Long reservationId, Long attributeId);

    @Modifying
    @Query("UPDATE VarcharType v SET v.value = ?3 WHERE v.reservationId = ?1 AND " +
            "v.attributeId = ?2")
    void updateField(Long reservationId, Long attributeId, String value);
}

