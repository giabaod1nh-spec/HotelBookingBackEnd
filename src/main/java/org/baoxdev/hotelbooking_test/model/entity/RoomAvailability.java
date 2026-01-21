package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.FetchProfile;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "room_availability")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String roomAvailId;

    Date date;

    Integer availableCount;

    BigDecimal price ;

    Integer version;

    @ManyToOne(fetch = FetchType.LAZY)
    RoomType roomType;

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    Instant createdAt;

    @LastModifiedDate
    @Column(name = "update_at")
    Instant updateAt;
}
