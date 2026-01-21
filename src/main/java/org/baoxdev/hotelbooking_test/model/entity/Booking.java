package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.BookingStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.sql.SQLType;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String bookingId;

    String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @OneToMany(mappedBy = "booking" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<BookingRooms> bookingRooms;

    Date checkInDate ;

    Date checkOutDate;

    @Column(name = "total_price" , precision = 10 , scale = 2)
    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    BookingStatus bookingStatus;

    Integer numGuest;

    String guestName;

    String guestEmail;

    String guestPhone;

    String specialRequest;

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    Instant updatedAt;
}
