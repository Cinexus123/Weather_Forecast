package com.example.demo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "LocalDate is in wrong format. Must be in format: yyyy-MM-dd")

public class LocalDateParseException extends RuntimeException {

    public LocalDateParseException(String localDate) {
        super("LocalDate: " + localDate + " is in wrong format");
        log.warn("LocalDate: " + localDate + " is in wrong format");
    }
}
