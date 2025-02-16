package com.sangto.rental_car_server.domain.entity;

import com.sangto.rental_car_server.domain.enums.ETransactionStatus;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer id;

    @ManyToOne(targetEntity = Wallet.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_id")
    private Wallet wallet;

    @ManyToOne(targetEntity = Booking.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    private Booking booking;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private ETransactionType type;

    @Enumerated(EnumType.STRING)
    private ETransactionStatus status;

    private String description;
    private String error_message;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date transaction_date;
}
