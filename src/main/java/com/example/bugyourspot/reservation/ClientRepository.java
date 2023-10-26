package com.example.bugyourspot.reservation;

import com.example.bugyourspot.reservation.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {


}

