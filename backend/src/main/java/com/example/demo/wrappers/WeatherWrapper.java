package com.example.demo.wrappers;

import com.example.demo.entities.City;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WeatherWrapper {

   /* private Long id;*/
    private Double temp;
    private Double wind_spd;
    private LocalDate datetime;
    private City city;
}
