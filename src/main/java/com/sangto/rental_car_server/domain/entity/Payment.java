package com.sangto.rental_car_server.domain.entity;

import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer id;

    @ManyToOne(targetEntity = Booking.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    private Booking booking;

    private Double amount;
    @Enumerated(EnumType.STRING)
    private EPaymentType payment_type;

    @Enumerated(EnumType.STRING)
    private EPaymentMethod payment_method;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date transaction_date;
}
