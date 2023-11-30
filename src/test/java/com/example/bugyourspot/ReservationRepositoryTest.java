package com.example.bugyourspot;
import com.example.bugyourspot.reservation.Reservation;
import com.example.bugyourspot.reservation.ReservationRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testFindReservationsByClientId() {
        Reservation reservation = new Reservation();
        reservation.setClientId(1L);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setNumSlots(2);
        entityManager.persist(reservation);
        entityManager.flush();
        List<Reservation> reservations = reservationRepository.findByClientId(1L);

        assertEquals(1, reservations.size());
        assertEquals(1L, reservations.get(0).getClientId());
    }

    @Test
    public void testFindByReservationId() {
        Reservation reservation = new Reservation();
        reservation.setClientId(2L);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setNumSlots(2);
        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservations = reservationRepository.findByClientId(2L);
        assertEquals(1, reservations.size());
        Reservation foundReservation = reservations.get(0);

        Long reservationId = foundReservation.getReservationId();
        Reservation matchedReservation = reservationRepository.findByReservationId(reservationId);
        assertEquals(2L, matchedReservation.getClientId());
    }

    @Test
    public void testUpdateStartTime() {
        Reservation reservation = new Reservation();
        reservation.setStartTime(LocalDateTime.now());
        reservation.setNumSlots(2);
        reservation.setClientId(3L);
        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservations = reservationRepository.findByClientId(3L);
        Reservation updatedReservation = reservations.get(0);
        Long reservationId = updatedReservation.getReservationId();
        LocalDateTime newTime = LocalDateTime.now().plusHours(1);
        reservationRepository.updateStartTime(reservationId, newTime);
        entityManager.refresh(updatedReservation);

        Long roomForError = 1000L;
        assertTrue(Duration.between(newTime, updatedReservation.getStartTime()).toMillis() < roomForError);
    }

    @Test
    public void testUpdateNumSlots() {
        Reservation reservation = new Reservation();
        reservation.setStartTime(LocalDateTime.now());
        reservation.setNumSlots(2);
        reservation.setClientId(4L);
        entityManager.persist(reservation);
        entityManager.flush();

        List<Reservation> reservations = reservationRepository.findByClientId(4L);
        Reservation updatedReservation = reservations.get(0);
        Long reservationId = updatedReservation.getReservationId();
        reservationRepository.updateNumSlots(reservationId, 3);
        entityManager.refresh(updatedReservation);

        assertEquals(3, updatedReservation.getNumSlots());
    }
}