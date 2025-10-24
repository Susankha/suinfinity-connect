package com.suinfinity.payment.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleNoResourceFoundException(
      NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String errorMessage = "Resource not found : " + ex.getResourcePath();
    ErrorResponse errorResponse = getErrorResponse(status.value(), errorMessage, request);
    log.error(errorMessage, ex);
    return ResponseEntity.status(status.value()).headers(headers).body(errorResponse);
  }

  @ExceptionHandler(PaymentNotFoundException.class)
  protected ResponseEntity<Object> handlePaymentNotFoundException(
      PaymentNotFoundException ex, WebRequest request) {
    String errorMessage = ex.getMessage();
    ErrorResponse errorResponse =
        getErrorResponse(HttpStatus.NOT_FOUND.value(), errorMessage, request);
    log.error(errorMessage, ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest webRequest) {
    ErrorResponse errorResponse =
        getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), webRequest);
    String errorMessage = "Internal error : " + ex.getMessage();
    log.error(errorMessage, ex);
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
