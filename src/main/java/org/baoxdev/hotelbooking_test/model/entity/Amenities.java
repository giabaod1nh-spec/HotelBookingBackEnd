package org.baoxdev.hotelbooking_test.model.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.AmenityType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Amenities {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String amenityId;

    String amenityName;

    String amenityIcon ;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    AmenityType amenityType;

    String amenityDesc;

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    LocalDateTime createdAt;

    Boolean isDeleted ;
}
