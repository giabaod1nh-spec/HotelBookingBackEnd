package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;

import java.util.Date;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long tokenId;

    @Column(nullable = false , unique = true , length = 1000)
    String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId" , nullable = false)
    User user;

    Date sessionExpiryTime;














































}
