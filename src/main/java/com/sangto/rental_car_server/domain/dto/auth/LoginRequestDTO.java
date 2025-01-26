package com.sangto.rental_car_server.domain.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequestDTO(
      @NotBlank(message = "Email is required.")
      @Email(message = "Please enter a valid email address.")
      String email,
      @NotBlank(message = "Password is requied.")
      String password
) {
}
