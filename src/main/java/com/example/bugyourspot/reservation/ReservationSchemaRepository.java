package com.example.bugyourspot.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationSchemaRepository
        extends JpaRepository<ReservationSchema, Long> {

    @Query("SELECT rs FROM ReservationSchema rs WHERE rs.clientId = ?1")
    Optional<ReservationSchema> findReservationSchemaByClientId(Long clientId);
}
