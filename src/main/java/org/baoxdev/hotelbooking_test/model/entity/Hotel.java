package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hotels")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String hotelId;

    String hotelName;

    String hotelDescription;

    String hotelAddress;

    String hotelCity;

    String hotelCountry;

    String hotelPhone;

    String hotelEmail;

    Integer starRating;

    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<Room> rooms;

    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<RoomType> roomTypes;

    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<HotelImages> hotelImages;

    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<Booking> bookings;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    HotelStatus hotelStatus;

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    Instant updatedAt;

}
