package com.example.bugyourspot.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoubleTypeRepository extends JpaRepository<DoubleType, Long> {

}

