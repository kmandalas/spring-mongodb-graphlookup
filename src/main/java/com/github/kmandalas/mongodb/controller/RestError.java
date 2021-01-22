package com.github.kmandalas.mongodb.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@ToString
public class RestError {

  @Getter
  @Setter
  @ToString
  public static class FieldError {
    private String field;
    private String message;
  }

  private Instant timestamp;

  private Integer status;

  private String type;

  private String exception;

  private List<FieldError> errors;

  private String message;
}
