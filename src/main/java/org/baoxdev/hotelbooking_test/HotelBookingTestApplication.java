package org.baoxdev.hotelbooking_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HotelBookingTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelBookingTestApplication.class, args);
    }

}
