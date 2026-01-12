package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private  Boolean isDeleted;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "delete_at")
    private Instant deleteAt;

    @ManyToMany(fetch = FetchType.EAGER )
    private Set<Role> roles;

}
