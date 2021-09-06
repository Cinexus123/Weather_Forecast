package com.example.demo.services;

import com.example.demo.entities.City;

import java.util.List;

public interface CityService {

    List<City> getAll();

    City saveNewCity(String city, String country);

    City save(City save);

    City getCityAndDelete(String name);
}
