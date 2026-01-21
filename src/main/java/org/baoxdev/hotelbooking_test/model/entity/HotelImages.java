package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelImages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String hotelImageId;

    String hotelImageUrl;

    Boolean isPrimary;

    @ManyToOne(fetch = FetchType.LAZY)
    Hotel hotel;
}
