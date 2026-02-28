package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.bytebuddy.build.Plugin;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.swing.text.StyleContext;
import java.time.Instant;
import java.util.List;
import java.util.Set;

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

    Double distanceToCenterKm;

    Double avgRating;

    Integer TotalReviews;

    Double adjustRating;

    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<Room> rooms;

    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<RoomType> roomTypes;

    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<HotelImages> hotelImages;

    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<Booking> bookings;

    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<Review> reviews;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Amenities> amenities;

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
