package com.example.bugyourspot.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface reservationCustomValueRepository extends JpaRepository<Attribute, Long> {

    // TODO: find all custom values for a reservation
    // TODO: find a custom value for a custom field for a reservation
}


