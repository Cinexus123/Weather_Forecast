package com.example.demo.controllers;

import com.example.demo.config.properties.Globals;
import com.example.demo.config.properties.LogMessages;
import com.example.demo.entities.Weather;
import com.example.demo.exceptions.LocalDateParseException;
import com.example.demo.exceptions.UpdateDataInDatabaseException;
import com.example.demo.services.WeatherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/weather")
public class WeatherController {

    private WeatherService weatherService;

    @GetMapping()
    public ResponseEntity<List<Weather>> getAll() {
        log.info(LogMessages.getAllWeather);
        List<Weather> weathers = weatherService.getAll();
        return (weathers.isEmpty()) ? ResponseEntity.noContent().build() : ResponseEntity.ok(weathers);
    }

    @GetMapping("/{localdate}")
    public ResponseEntity<Weather> getBestCityForSurfing(@PathVariable("localdate") String localDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);

        if (!Globals.updateDataLock) {
            try {
                log.info(LogMessages.parseDate);
                format.parse(localDate);
                Weather weather = weatherService.getBestWeatherInCityForSurfing(localDate);
                return new ResponseEntity<>(weather, HttpStatus.OK);

            } catch (ParseException e) {
                throw new LocalDateParseException(localDate);

            } catch (NoSuchElementException e) {
                return ResponseEntity.noContent().build();
            }
        } else {
            throw new UpdateDataInDatabaseException();
        }
    }

    @GetMapping("/data/{city}/{country}")
    public ResponseEntity<List<Weather>> getCityWeather(@PathVariable("city") String city, @PathVariable("country") String country) throws Exception {
        if (!Globals.updateDataLock) {
            List<Weather> weather = weatherService.getListOfWeatherForCity(city, country);
            log.info(LogMessages.getLongWeatherForCity);
            return ResponseEntity.ok(weather);
        } else {
            throw new UpdateDataInDatabaseException();
        }
    }

    @PostMapping(value = "/saveWeatherObject")
    public ResponseEntity<Weather> saveWeatherObject(@RequestBody Weather weather) {
        Weather weatherToSave = weatherService.save(weather);
        log.info(LogMessages.request + weather);
        return new ResponseEntity<>(weatherToSave, HttpStatus.OK);
    }
}

