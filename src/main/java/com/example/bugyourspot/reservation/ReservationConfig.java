package com.example.bugyourspot.reservation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Configuration
public class ReservationConfig {

    /*
    @Bean
    CommandLineRunner commandLineRunner(ReservationRepository repository) {
        return args -> {
            Reservation reservation1 = new Reservation(
                    55L,
                    1L,
                    LocalDateTime.now(),
                    5
            );

            Reservation reservation2 = new Reservation(
                    4113L,
                    1L,
                    LocalDateTime.now(),
                    2
            );

            repository.saveAll(
                    List.of(reservation1, reservation2)
            );

        };
    }
    */
}
