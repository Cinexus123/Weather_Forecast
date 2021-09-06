package com.example.demo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "URL problem with pair: city and country parameters")
public class URLCreateAPIException extends RuntimeException {

    public URLCreateAPIException(String city, String country) {
        super("URLCreateAPIException occured for city: " + city + "and country: " + country);
        log.warn("URLCreateAPIException occured for city: " + city + "and country: " + country);
    }
}
