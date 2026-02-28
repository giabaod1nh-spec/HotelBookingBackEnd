package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewImages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String reviewImageId;

    String imageUrl;

    @CreatedDate
    @Column(name = "created_at" , nullable = false)
    Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId") //Forgein key
    Review review;
}
