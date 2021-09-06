package com.example.demo.wrappers;

import lombok.Data;

import java.util.List;

@Data
public class CityWrapper {

    private String city_name;
    private String country_code;
    private Double lon;
    private Double lat;
    private List<WeatherWrapper> data;
}
