package com.example.bugyourspot.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

//    @Query("SELECT r FROM Reservation r WHERE r.clientId = ?1")
    java.util.List<Reservation> findByClientId(Long clientId);

    @Query("SELECT r from Reservation r WHERE r.reservationId = ?1")
    Reservation findByReservationId(Long reservationId);

    @Query("UPDATE Reservation r SET r.startTime = ?1 WHERE r.reservationId = ?1")
    void updateStartTime(Long reservationId, LocalDateTime newTime);

    @Query("UPDATE Reservation r SET r.numSlots = ?1 WHERE r.reservationId = ?1")
    void updateNumSlots(Long reservationId, int numSlots);
}
