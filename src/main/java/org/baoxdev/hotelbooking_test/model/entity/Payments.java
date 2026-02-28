package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.PayStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "payment")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String paymentId;

    String transactionNo;

    String txnRef;

    String bankCode;

    String cardType;

    @Column(name = "pay_price" , precision = 10 , scale = 2)
    BigDecimal amountPayment;

    @Enumerated(EnumType.STRING)
    PayStatus payStatus;

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;
}
