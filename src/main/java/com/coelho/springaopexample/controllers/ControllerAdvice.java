package com.coelho.springaopexample.controllers;

import com.coelho.springaopexample.dtos.APIError;
import com.coelho.springaopexample.dtos.APIErrorMessage;
import com.coelho.springaopexample.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<APIError> handleBusinessException(BusinessException businessException) {
        log.error("Handling handleBusinessException []", businessException);

        Collection<APIErrorMessage> messages = List.of(APIErrorMessage.builder()
                                                                      .code(businessException.getKey())
                                                                      .description(businessException
                                                                              .getKey())
                                                                      .build());

        return ResponseEntity.status(businessException.getHttpStatus())
                             .body(buildAPIError(messages));
    }

    @ExceptionHandler({MissingRequestHeaderException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException validException, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        log.error("Handling handleMethodArgumentNotValid []", validException);

        Collection<APIErrorMessage> messages = validException.getBindingResult().getAllErrors()
                                                             .stream()
                                                             .map(error -> APIErrorMessage.builder()
                                                                                          .code(error
                                                                                                  .getDefaultMessage())
                                                                                          .description(error
                                                                                                  .getDefaultMessage())
                                                                                          .build())
                                                             .collect(Collectors.toList());

        APIError apiError = buildAPIError(messages);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIError handleException(Exception exception) {
        log.error("Handling", exception);

        Collection<APIErrorMessage> messages = List.of(APIErrorMessage.builder()
                                                                      .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
                                                                      .description(HttpStatus.INTERNAL_SERVER_ERROR
                                                                              .getReasonPhrase())
                                                                      .build());

        return buildAPIError(messages);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintException(ConstraintViolationException validException) {
        log.error("Handling", validException);

        Collection<APIErrorMessage> messages = validException.getConstraintViolations()
                                                             .stream()
                                                             .map(error -> APIErrorMessage.builder()
                                                                                          .code(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                                                                                          .description(error
                                                                                                  .getMessage())
                                                                                          .build())
                                                             .collect(Collectors.toList());

        var apiError = buildAPIError(messages);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(apiError);
    }

    private APIError buildAPIError(Collection<APIErrorMessage> messages) {
        return APIError.builder()
                       .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                       .messages(messages)
                       .build();
    }
}