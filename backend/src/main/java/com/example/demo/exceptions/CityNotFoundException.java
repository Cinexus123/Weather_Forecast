package com.example.demo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "City or country not found ")
public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String city, String country) {
        super("City: " + city + " and " + "country: " + country + " not found");
        log.warn("City: " + city + " and " + "country: " + country + " not found");
    }
}
