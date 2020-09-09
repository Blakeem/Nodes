package com.assignment.node;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        // payload with exception details
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(e.getMessage(),
            badRequest, 
            ZonedDateTime.now(ZoneId.of("Z")));
        // return response entity
        return new ResponseEntity<>(apiException, badRequest);
    }
    
        @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
        public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e){
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;
            ApiException apiException = new ApiException(e.getLocalizedMessage(),
                badRequest, 
                ZonedDateTime.now(ZoneId.of("Z")));
            return new ResponseEntity<>(apiException, badRequest);
        }
    
        @ExceptionHandler({ HttpMessageNotReadableException.class })
        public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e){
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;
            ApiException apiException = new ApiException(e.getLocalizedMessage(),
                badRequest, 
                ZonedDateTime.now(ZoneId.of("Z")));
            return new ResponseEntity<>(apiException, badRequest);
        }
}
