package com.janterertube.videos.handler;

import com.janterertube.videos.exceptions.InvalidFileFormatException;
import com.janterertube.videos.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class AppControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidFileFormatException(InvalidFileFormatException e, WebRequest request) {
        return buildApiErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<ApiErrorResponse> buildApiErrorResponse(Exception ex, HttpStatus httpStatus, WebRequest request) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .status(httpStatus)
                .error(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .path(request.getContextPath())
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }
}
