package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {

    @Id
    private String name;
    private String country_code;

    private Double lon;
    private Double lat;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Weather> data;
}

