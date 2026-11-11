package com.issueflow.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ApiException.class)
  ResponseEntity<?> api(ApiException e) {
    return ResponseEntity.status(e.getStatus()).body(Map.of("timestamp", Instant.now(), "status", e.getStatus().value(), "error", e.getStatus().getReasonPhrase(), "message", e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<?> validation(MethodArgumentNotValidException e) {
    Map<String, String> errors = new LinkedHashMap<>();
    e.getBindingResult().getFieldErrors().forEach(x -> errors.put(x.getField(), x.getDefaultMessage()));
    return ResponseEntity.badRequest().body(Map.of("timestamp", Instant.now(), "status", 400, "error", "Validation failed", "fieldErrors", errors));
  }

  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<?> denied() {
    return ResponseEntity.status(403).body(Map.of("status", 403, "error", "Forbidden", "message", "You are not allowed to perform this action."));
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<?> unexpected(Exception e) {
    return ResponseEntity.status(500).body(Map.of("timestamp", Instant.now(), "status", 500, "error", "Internal Server Error", "message", "An unexpected error occurred."));
  }
}
