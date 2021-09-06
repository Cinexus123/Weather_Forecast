package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    @Id
    private Long id;

    private Double temp;
    private Double wind_spd;
    private LocalDate datetime;

    private String city;
}
