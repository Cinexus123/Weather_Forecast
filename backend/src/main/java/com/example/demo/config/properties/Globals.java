package com.example.demo.config.properties;

public class Globals {

    public static final String URL = "https://api.weatherbit.io/v2.0/forecast/daily?";
    public static final String TEST_URL = "https://api.weatherbit.io/v2.0/forecast/daily?city=Wroclaw&country=PL&key=51fd34e0f0d34fdc94686c06bbe40883";
    public static final String GET_WEATHER = "http://localhost:5001/weather/";
    public static final String GET_CITY = "http://localhost:5001/city/";
    public static final String SAVE_WEATHER_OBJECT = "http://localhost:5001/weather/saveWeatherObject";
    public static final String SAVE_CITY_OBJECT = "http://localhost:5001/city/saveCityObject";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String KEY = "key";
    public static Boolean updateDataLock = false;

}
