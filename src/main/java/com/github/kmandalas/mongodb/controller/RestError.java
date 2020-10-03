package com.github.kmandalas.mongodb.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

/**
 * The rest error.
 *
 * @author : Manos Papantonakos.
 */
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

  /**
   * The timestamp.
   */
  private Instant timestamp;

  /**
   * The status.
   */
  private Integer status;

  /**
   * The type.
   */
  private String type;

  /**
   * The exception.
   */
  private String exception;

  /**
   * The errors.
   */
  private List<FieldError> errors;

  /**
   * The message.
   */
  private String message;
}
