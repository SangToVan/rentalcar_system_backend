package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "car")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @Column(name = "car_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_owner_id", referencedColumnName = "user_id")
    private User car_owner;

    private String plate_number;
    private String name;
    private String brand;
    private String model;
    private String color;
    private Integer production_year;
    private Integer seat_number;

    @Enumerated(EnumType.STRING)
    private ECarTransmission car_transmission;

    @Enumerated(EnumType.STRING)
    private EFuelType fuel_type;

    private Float fuel_consumption;
    private Integer mileage;
    private String additional_functions;
    private String description;
    private String terms_of_use;

//    @ManyToOne(
//            targetEntity = Location.class,
//            fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_id", referencedColumnName = "location_id", nullable = true)
//    private Location location;
    private String address;

    private Double price_per_day;
    private Boolean availability;

    @Enumerated(EnumType.STRING)
    private ECarStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date updated_at;

    private Double rating;

    @JsonIgnore
    @OneToMany(
            mappedBy = "car",
            targetEntity = Booking.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "car",
            targetEntity = Image.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "car",
            targetEntity = Maintenance.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Maintenance> maintenances = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "car",
            targetEntity = Feedback.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
        image.setCar(this);
    }
}
