package com.example.demo.controllers;

import com.example.demo.config.properties.Globals;
import com.example.demo.config.properties.LogMessages;
import com.example.demo.entities.City;
import com.example.demo.exceptions.UpdateDataInDatabaseException;
import com.example.demo.services.CityService;
import com.example.demo.services.WeatherService;
import com.example.demo.services.impl.WeatherServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/city")
public class CityController {

    private CityService cityService;
    private WeatherService weatherService;

    @GetMapping()
    public ResponseEntity<List<City>> getCurrentCitiesInDatabase() {
        List<City> city = cityService.getAll();
        log.info(LogMessages.getCityInDb);
        return (city.isEmpty()) ? ResponseEntity.noContent().build() : ResponseEntity.ok(city);
    }

    @PostMapping("/addNewCity/{city}/{country}")
    public ResponseEntity<City> saveNewCity(@PathVariable("city") String city, @PathVariable("country") String country) {
        if (!Globals.updateDataLock) {
            List<City> listWithCities = cityService.getAll();

            for (City element : listWithCities) {
                if (element.getName().equals(city)) {
                    return ResponseEntity.noContent().build();
                }
            }
            City newCity = cityService.saveNewCity(city, country);
            log.info(LogMessages.saveNewCity);
            return new ResponseEntity<>(newCity, HttpStatus.OK);
        } else {
            throw new UpdateDataInDatabaseException();
        }
    }

    @PostMapping(value = "/saveCityObject")
    public ResponseEntity<City> saveCityObject(@RequestBody City city) {
        City cityToSave = cityService.save(city);
        log.info(LogMessages.request + city);
        return new ResponseEntity<>(cityToSave, HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<City> deleteCity(@PathVariable("name") String name) {
        if (!Globals.updateDataLock) {
            City city = cityService.getCityAndDelete(name);
            if (city == null) {
                return ResponseEntity.notFound().build();
            }
            WeatherServiceImpl.recordsInCityTable = WeatherServiceImpl.recordsInCityTable - 1;
            weatherService.getWeatherAndDelete(name);
            return ResponseEntity.ok(city);
        } else {
            throw new UpdateDataInDatabaseException();
        }
    }
}
