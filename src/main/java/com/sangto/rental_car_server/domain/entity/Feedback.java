package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "car_id")
    private Car car;

    @JsonIgnore
    @OneToOne(targetEntity = Booking.class, fetch = FetchType.EAGER, orphanRemoval = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private Double rating;
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date created_at;
}
