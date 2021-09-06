package com.example.demo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "URI problem with pair: city and country parameters")
public class URLFormException extends RuntimeException {

    public URLFormException(String city, String country) {
        super("URLFormException occured for city: " + city + "and country: " + country);
        log.warn("URLFormException occured for city: " + city + "and country: " + country);
    }
}
