package com.example.demo.services.impl;

import com.example.demo.entities.City;
import com.example.demo.exceptions.CityNotFoundException;
import com.example.demo.repositories.CityRepository;
import com.example.demo.services.CityService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }

    @Override
    public City saveNewCity(String city, String country) {
        return cityRepository.save(new City(
                city,
                country,
                null,
                null,
                null));
    }

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    public City getCityAndDelete(String name) {
        List<City> cities = cityRepository.findAll();
        Integer id = null;
        for (int cityId = 0; cityId < cities.size(); cityId++) {
            if (cities.get(Math.toIntExact(cityId)).getName().equals(name)) {
                id = cityId;
            }
        }
        if (id == null) {
            throw new CityNotFoundException(name, null);
        }
        cityRepository.delete(cities.get(id));
        return cities.get(id);
    }
}

