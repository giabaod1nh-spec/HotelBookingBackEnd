package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "email_verify_token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id ;

    @Column(nullable = false , unique = true)
    String emailVerifyToken;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    Instant expiredAt;
}
