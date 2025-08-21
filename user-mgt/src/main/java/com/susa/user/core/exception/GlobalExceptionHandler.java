package com.susa.user.core.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .toList();
    String errorMessage = "Validation failed";
    ErrorResponse errorResponse = getErrorResponse(status.value(), errorMessage, request);
    errorResponse.setErrors(errors);
    logger.error(errorMessage + " : ", ex);
    return ResponseEntity.status(status.value()).headers(headers).body(errorResponse);
  }

  @Override
  protected ResponseEntity<Object> handleNoResourceFoundException(
      NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String errorMessage = "Resource not found : " + ex.getResourcePath();
    ErrorResponse errorResponse = getErrorResponse(status.value(), errorMessage, request);
    logger.error(errorMessage, ex);
    return ResponseEntity.status(status.value()).headers(headers).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest webRequest) {
    ErrorResponse errorResponse =
        getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), webRequest);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(errorResponse);
  }

  private String getTimeStamp() {
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a 'UTC'");

    return Instant.now().atZone(ZoneOffset.UTC).format(dateTimeFormatter);
  }

  private ErrorResponse getErrorResponse(int status, String errorMessage, WebRequest request) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timeStamp(getTimeStamp())
            .status(status)
            .errorMessage(errorMessage)
            .path(((ServletWebRequest) request).getRequest().getRequestURI())
            .build();

    return errorResponse;
  }
}

@Data
@Builder
class ErrorResponse {

  @NonNull private String timeStamp;
  private int status;
  @NonNull private String errorMessage;

  @JsonInclude(Include.NON_NULL)
  private List<String> errors;

  private String path;
}
