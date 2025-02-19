package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.role = :role")
    Optional<User> findUserByIdAndUserType(@Param("id") Integer id, @Param("role") EUserRole role);

    @Query("SELECT u FROM User u WHERE u.role != :role")
    Page<User> findAllExcludingRole(@Param("role") EUserRole role, Pageable pageable);

    int countAllBy();
}
