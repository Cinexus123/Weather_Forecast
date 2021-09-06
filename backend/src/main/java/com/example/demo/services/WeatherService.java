package com.example.demo.services;

import com.example.demo.entities.Weather;

import java.util.List;

public interface WeatherService {

    List<Weather> getListOfWeatherForCity(String city, String country) throws Exception;

    Weather getBestWeatherInCityForSurfing(String localDate);

    void setParamsForNewRecordsInDatabase() throws Exception;

    void refreshAllRecordsInDb() throws Exception;

    List<Weather> getAll();

    Weather save(Weather weather);

    List<Weather> getWeatherAndDelete(String name);
}
