package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id")
    private User customer;

    @ManyToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "car_id")
    private Car car;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
    private Date booking_date;

    @DateTimeFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
    private LocalDateTime start_date;

    @DateTimeFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
    private LocalDateTime end_date;

    private Double total_price;

    private Double rental_fee;
    private Double service_fee = 0.0;

    @Enumerated(EnumType.STRING)
    private EBookingStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date updated_at;

    @JsonIgnore
    @OneToMany(
            mappedBy = "booking",
            targetEntity = Complaint.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Complaint> complaints = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "booking",
            targetEntity = Payment.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "booking",
            targetEntity = Transaction.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "booking", orphanRemoval = true)
    private Feedback feedback;


}
