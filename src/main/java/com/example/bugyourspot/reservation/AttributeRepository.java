package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    @Query("SELECT a FROM Attribute a WHERE a.clientId = ?1 AND a.label = ?2")
    Attribute findAttributeByClientAndTitle (Long clientId, String label);

    java.util.List<Attribute> findByClientId(Long clientId);
}

