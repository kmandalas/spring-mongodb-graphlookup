package com.github.kmandalas.mongodb.controller;

import java.time.Instant;
import java.util.List;

/**
 * The rest error.
 *
 * @author : Manos Papantonakos.
 */
public class RestError {

  public class FieldError {
    private String field;
    private String message;

    public String getField() {
      return field;
    }

    public void setField(final String field) {
      this.field = field;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(final String message) {
      this.message = message;
    }

    @Override
    public String toString() {
      return "FieldError{" +
              "field='" + field + '\'' +
              ", message='" + message + '\'' +
              '}';
    }
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

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final Instant timestamp) {
    this.timestamp = timestamp;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(final Integer status) {
    this.status = status;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getException() {
    return exception;
  }

  public void setException(final String exception) {
    this.exception = exception;
  }

  public List<FieldError> getErrors() {
    return errors;
  }

  public void setErrors(final List<FieldError> errors) {
    this.errors = errors;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "RestError{" +
            "timestamp=" + timestamp +
            ", status=" + status +
            ", type='" + type + '\'' +
            ", exception='" + exception + '\'' +
            ", errors=" + errors +
            ", message='" + message + '\'' +
            '}';
  }
}
