package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import org.baoxdev.hotelbooking_test.model.enums.Status;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String hotelId;

    private String hotelName;

    private String hotelDescription;

    private String hotelAddress;

    private String hotelCity;

    private String hotelCountry;

    private String hotelPhone;

    private String hotelEmail;

    private Integer starRating;

    @ColumnDefault("'ACTIVE'")
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}
