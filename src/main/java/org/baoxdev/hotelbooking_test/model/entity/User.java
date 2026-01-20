package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.baoxdev.hotelbooking_test.model.enums.UserStatus;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Builder
@Getter
@Setter

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(name = "email" , nullable = true)
    private String email;

    @Column(name = "user_name" , nullable = true)
    private String userName;

    @Column(name = "password" , nullable = false)
    private String password;

    @Column(name = "first_name" , nullable = false)
    private String firstName;

    @Column(name = "last_name" , nullable = false)
    private String lastName;

    @Column(name = "avatar_url" , nullable = true)
    private String avatar_url;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)

    private UserStatus userStatus;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @CreatedDate
    @Column(name = "delete_at")
    private Instant deleteAt;

    @ManyToMany(fetch = FetchType.EAGER )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<RefreshToken> tokens;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Booking> bookings;
}
