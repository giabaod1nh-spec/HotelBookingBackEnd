package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.RoomStatus;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "rooms")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotelId") //FK
    Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomTypeId") //Fk
    RoomType roomType;

    @OneToMany(mappedBy = "room" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<BookingRooms> bookingRooms;

    String roomNumber;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    RoomStatus roomStatus ;

    @CreatedDate
    @Column(name = "created_at" , nullable = false)
    Instant createdAt;

    @CreatedDate
    @Column(name = "update_at" , nullable = false)
    Instant updateAt;
}
