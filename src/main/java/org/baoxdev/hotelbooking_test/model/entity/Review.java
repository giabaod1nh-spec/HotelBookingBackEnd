package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String reviewId;

    Integer reviewRating;

    String reviewPositiveComment;

    String reviewNegativeComment;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL , orphanRemoval = true)
    List<ReviewImages> reviewImagesList;

    //Moi quan he @ManytoOne voi User , hotel , booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotelId")
    Hotel hotel ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingId")
    Booking booking;

}
