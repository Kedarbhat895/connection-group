package com.example.connection_group.controller_advice;

import com.example.connection_group.exceptions.DuplicateConnectionGroupException;
import com.example.connection_group.exceptions.DuplicateNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ConnectionGroupControllerAdvice {

  private static final Logger log = LoggerFactory.getLogger(ConnectionGroupControllerAdvice.class);

  @ExceptionHandler(DuplicateConnectionGroupException.class)
  public ResponseEntity<String> handleDuplicateConnectionGroupException(
      DuplicateConnectionGroupException ex, WebRequest request) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DuplicateNodeException.class)
  public ResponseEntity<String> handleDuplicateNodeException(DuplicateNodeException ex,
      WebRequest request) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex,
      WebRequest request) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}