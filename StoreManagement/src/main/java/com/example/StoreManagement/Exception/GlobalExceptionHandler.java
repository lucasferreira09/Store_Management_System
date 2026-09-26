package com.example.StoreManagement.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ProblemExceptionResponse handleApiExceptions(ApiException e) {

        ProblemExceptionResponse response = new ProblemExceptionResponse(e);
        response.setDetail(e.getMessage());

        return response;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemExceptionResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ProblemExceptionResponse response = new ProblemExceptionResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request content",
                "validation-error",
                errors
        );
        return response;
    }

}
