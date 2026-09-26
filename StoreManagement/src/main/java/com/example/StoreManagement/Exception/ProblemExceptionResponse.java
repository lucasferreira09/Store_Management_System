package com.example.StoreManagement.Exception;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProblemExceptionResponse extends ProblemDetail {

    public ProblemExceptionResponse(ApiException e) {
        setStatus(e.getStatus().value());
        setType(URI.create(e.getType()));
        setProperty("timestamp", Instant.now());
    }

    public ProblemExceptionResponse(
            @NonNull HttpStatus status,
            String detail,
            String type
            //Map<String, List<String>> errors
    ) {
        super(status.value());
        setDetail(detail);
        setType(URI.create(type));
        setProperty("timestamp", Instant.now());
        //setProperty("errors", errors);
    }

    // VErsion2
    public ProblemExceptionResponse(
            @NonNull HttpStatus status,
            String detail,
            String type,
            Map<String, String> errors
    ) {
        super(status.value());
        setDetail(detail);
        setType(URI.create(type));
        setProperty("timestamp", Instant.now());
        setProperty("errors", errors);
    }



    public String getFileName(ApiException e) {

        String entityName = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::getFileName)
                .filter(fileName -> fileName.endsWith("Service.java"))
                .findFirst()
                .map(fileName -> fileName.replace("Service.java", ""))
                .orElse("Unknown");;

        return entityName;
    }


}
