package com.example.demo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Database is currently update. Please try again in few seconds.")
public class UpdateDataInDatabaseException extends RuntimeException {

    public UpdateDataInDatabaseException() {
        super("Please try again in few seconds. We currently update the database");
        log.warn("Please try again in few seconds. We currently update the database");
    }
}
