package com.example.demo.repositories;

import com.example.demo.entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

}
