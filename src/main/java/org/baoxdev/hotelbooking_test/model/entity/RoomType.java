package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.RoomTypeStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.SQLType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String roomTypeId;

    String roomTypeName;

    String roomTypeDesc;

    @Column(name = "base_price" , precision = 10 , scale = 2)
    BigDecimal basePrice;

    Integer maxOccupy;

    Integer totalRooms;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @LastModifiedDate
    @Column(name = "update_at")
    Instant updateAt;

   @Enumerated(EnumType.STRING)
   //@JdbcTypeCode(SqlTypes.ENUM)
   RoomTypeStatus roomTypeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id") //FK
    Hotel hotel;


    @OneToMany(mappedBy =  "roomType" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<Room> rooms;

    @OneToMany(mappedBy = "roomType" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<RoomTypeImages> roomTypeImages;

    @OneToMany(mappedBy = "roomType" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<BookingRooms> bookingRooms;

    @OneToMany(mappedBy = "roomType" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<RoomAvailability> roomAvailabilities;
}
