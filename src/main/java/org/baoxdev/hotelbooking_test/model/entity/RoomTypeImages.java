package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoomTypeImages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String roomTypeImageId;

    String roomImageUrl;


    Boolean isPrimary;
    @ManyToOne(fetch = FetchType.LAZY)
    RoomType roomType;
}
