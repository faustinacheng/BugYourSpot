package com.example.bugyourspot.reservation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class ReservationConfig {

    @Bean
    CommandLineRunner commandLineRunner(ReservationRepository repository) {
        return args -> {
            Reservation reservation1 = new Reservation(
                    4156,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            Reservation reservation2 = new Reservation(
                    4113,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            repository.saveAll(
                    List.of(reservation1, reservation2)
            );

        };
    }
}
