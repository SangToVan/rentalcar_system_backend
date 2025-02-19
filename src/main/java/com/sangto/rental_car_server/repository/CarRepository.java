package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query("SELECT DISTINCT c FROM Car c " + "JOIN FETCH c.car_owner co "
            + "LEFT JOIN FETCH c.images i "
            + "WHERE co.id = :ownerId")
    Page<Car> getListCarByOwner(@Param(("ownerId")) Integer ownerId, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Car c " + "JOIN FETCH c.car_owner co "
            + "LEFT JOIN FETCH c.images i "
            + "WHERE co.id = :ownerId "
            + "AND CONCAT(c.name,' ',c.brand,' ',c.model,' ',c.address) LIKE %:keyword%")
    Page<Car> getListCarByOwnerWithKeyword(@Param(("ownerId"))Integer ownerId, @Param(("keyword"))String keyword, Pageable pageable);

    Optional<Car> getCarById(Integer id);

    @Query(
            value =
                    "SELECT * FROM cars c\n"
                            + "WHERE `c.status` = 'ACTIVE' AND c.address LIKE %:address% AND c.car_id NOT IN (\n"
                            + "\t\t\t\t\tSELECT b.car_id FROM bookings b \n"
                            + "                    WHERE b.`status` != 'COMPLETED' AND b.`status` != 'CANCELLED' \n"
                            + "\t\t\t\t\t\tAND ((b.end_date >= :endTime AND b.start_date <= :endTime) \n"
                            + "\t\t\t\t\t\t\t\tOR (b.start_date <= :startTime AND b.end_date >= :startTime) \n"
                            + "                                OR (b.end_date <= :endTime AND b.start_date >= :startTime)))",
            nativeQuery = true)
    Page<Car> searchCar(
            @Param("address")String address,
            @Param("startTime")String start_time,
            @Param("endTime")String end_time,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * FROM cars c\n"
                            + "WHERE c.car_id = :carId AND `c.status` = 'ACTIVE' AND c.car_id NOT IN (\n"
                            + "\t\t\t\t\tSELECT b.car_id FROM bookings b \n"
                            + "                    WHERE b.`status` != 'COMPLETED' AND b.`status` != 'CANCELLED' \n"
                            + "\t\t\t\t\t\tAND ((b.end_date >= :endTime AND b.start_date <= :endTime) \n"
                            + "\t\t\t\t\t\t\t\tOR (b.start_date <= :startTime AND b.end_date >= :startTime) \n"
                            + "                                OR (b.end_date <= :endTime AND b.start_date >= :startTime)))",
            nativeQuery = true)
    Optional<Car> checkScheduleCar(
            @Param("carId")Integer id,
            @Param("startTime")String start_time,
            @Param("endTime")String end_time);

    @Query("SELECT DISTINCT c FROM Car c " + "JOIN FETCH c.car_owner co "
            + "LEFT JOIN FETCH c.images i "
            + "WHERE co.id = :ownerId")
    List<Car> getListCarByOwner(@Param(("ownerId"))Integer ownerId);

    int countAllBy();
}
