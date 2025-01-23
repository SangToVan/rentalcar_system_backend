package com.sangto.rental_car_server.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

  private Object[] args;

  public AppException() {
    super();
  }

  public AppException(String message) {
        super(message);
  }

  public AppException(String message, Object ... args) {
    super(message);
    this.args = args;
  }
}
