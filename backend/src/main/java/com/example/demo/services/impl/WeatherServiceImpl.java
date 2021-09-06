package com.example.demo.services.impl;

import com.example.demo.config.properties.Globals;
import com.example.demo.entities.City;
import com.example.demo.entities.Weather;
import com.example.demo.exceptions.URLCreateAPIException;
import com.example.demo.exceptions.URLFormException;
import com.example.demo.repositories.WeatherRepository;
import com.example.demo.services.WeatherService;
import com.example.demo.wrappers.CityWrapper;
import com.example.demo.wrappers.WeatherWrapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    List<City> emptyCitiesObjectInDb = new ArrayList<>();
    long currentRecordsInDb = 1L;
    long maxNewRecords = 1L;
    public static int recordsInCityTable = 0;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private CityServiceImpl cityServiceImpl;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${apiKey}")
    private String apiKey;

    @Override
    public List<Weather> getAll() {
        return weatherRepository.findAll();
    }

    @Override
    public Weather save(Weather weather) {
        return weatherRepository.save(weather);
    }

    @Override
    public List<Weather> getWeatherAndDelete(String name) {
        List<Weather> weather = weatherRepository.findAll();
        List<Integer> indexWithWeather = new ArrayList<>();
        List<Weather> deletedIndex = new ArrayList<>();

        for (int weatherId = 0; weatherId < weather.size(); weatherId++) {
            if (weather.get(Math.toIntExact(weatherId)).getCity().equals(name)) {
                indexWithWeather.add(weatherId);
            }
        }

        indexWithWeather
                .stream()
                .forEach(s -> {
                    weatherRepository.delete(weather.get(s));
                    deletedIndex.add(weather.get(s));
                });

        return deletedIndex;
    }

    public List<Weather> getListOfWeatherForCity(String city, String country) {

        List<Weather> weathers;

        log.info(getUrl(city, country));
        CityWrapper cityWrapper = restTemplate.getForObject(getUrl(city, country), CityWrapper.class);

        if (cityWrapper != null) {
            weathers = getWeatherForAppropriateDay(cityWrapper, city);
        } else {
            cityServiceImpl.getCityAndDelete(city);
            return null;
        }
        return weathers;
    }

    private String getUrl(String city, String country) {

        java.net.URL url;
        try {
            url = prepareApiUrlRequest(city, country);

        } catch (URISyntaxException e) {
            throw new URLFormException(city, country);
        } catch (MalformedURLException e) {
            throw new URLCreateAPIException(city, country);
        }
        return url.toString();
    }

    private URL prepareApiUrlRequest(String city, String country) throws URISyntaxException, MalformedURLException {
        URIBuilder uriBuilder = new URIBuilder(Globals.URL);

        uriBuilder.addParameter(Globals.CITY, city);
        uriBuilder.addParameter(Globals.COUNTRY, country);
        uriBuilder.addParameter(Globals.KEY, apiKey);

        return uriBuilder.build().toURL();
    }

    @Override
    public Weather getBestWeatherInCityForSurfing(String localDate) {
        ResponseEntity<List<Weather>> responseEntity =
                restTemplate.exchange(
                        Globals.GET_WEATHER,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        if (responseEntity.getBody() != null) {
            List<Weather> allRecordsInDb = responseEntity.getBody();
            Map<String, Double> cityNameAndResultMap = new HashMap<>();

            allRecordsInDb
                    .stream()
                    .filter(s -> s.getDatetime().toString().equals(localDate))
                    .filter(s -> s.getTemp() >= 5.0 && s.getTemp() <= 35.0)
                    .filter(s -> s.getWind_spd() >= 5.0 && s.getWind_spd() <= 18.0)
                    .forEach(s -> {
                        cityNameAndResultMap.put(s.getCity(), s.getWind_spd() * 3 + s.getTemp());
                    });

            Weather weather = findBestCityForSurfing(cityNameAndResultMap, allRecordsInDb, localDate);

            if (weather != null) {
                return weather;
            }
        }
        return null;
    }

    private Weather findBestCityForSurfing(Map<String, Double> cityNameAndResultMap, List<Weather> allRecordsInDb, String localDate) {
        String bestCityForSurfing = cityNameAndResultMap.entrySet()
                .stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

        for (Weather element : allRecordsInDb) {
            if (element.getDatetime().toString().equals(localDate) && element.getCity().equals(bestCityForSurfing)) {
                return element;
            }
        }
        return null;
    }

    @Override
    @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    public void setParamsForNewRecordsInDatabase() {
        Globals.updateDataLock = true;
        ResponseEntity<List<City>> responseEntity =
                restTemplate.exchange(
                        Globals.GET_CITY,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        List<City> cities = responseEntity.getBody();
        if (responseEntity.getBody() != null && recordsInCityTable != cities.size()) {
            emptyCitiesObjectInDb.clear();

            cities
                    .stream()
                    .filter(s -> s.getLat() == null)
                    .forEach(s -> emptyCitiesObjectInDb.add(s));

            if (maxNewRecords < currentRecordsInDb) {
                maxNewRecords = currentRecordsInDb;
            } else {
                currentRecordsInDb = maxNewRecords;
            }

            for (int counter = 0; counter < emptyCitiesObjectInDb.size(); counter++) {
                getListOfWeatherForCity(emptyCitiesObjectInDb.get(counter).getName(), emptyCitiesObjectInDb.get(counter).getCountry_code());
            }
            recordsInCityTable = cities.size();
        }
        Globals.updateDataLock = false;
    }

    @Override
    @Scheduled(initialDelay = 1800000, fixedDelay = 1800000) //30 min
    public void refreshAllRecordsInDb() {
        Globals.updateDataLock = true;
        currentRecordsInDb = 1L;
        ResponseEntity<List<City>> responseEntity =
                restTemplate.exchange(
                        Globals.GET_CITY,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        List<City> users = responseEntity.getBody();

        for (int counter = 0; counter < users.size(); counter++) {
            getListOfWeatherForCity(users.get(counter).getName(), users.get(counter).getCountry_code());
        }
        Globals.updateDataLock = false;
    }

    private List<Weather> getWeatherForAppropriateDay(CityWrapper cityWrapper, String city) {
        List<Weather> weathers = new ArrayList<>();

        saveWeatherObjectToDatabase(weathers, cityWrapper, city);
        saveCityObjectToDatabase(cityWrapper, weathers);

        return weathers;
    }

    public void saveWeatherObjectToDatabase(List<Weather> weathers, CityWrapper cityWrapper, String city) {
        for (WeatherWrapper elem : cityWrapper.getData()) {
            Weather weather = new Weather(
                    currentRecordsInDb,
                    elem.getTemp(),
                    elem.getWind_spd(),
                    elem.getDatetime(),
                    city);

            weathers.add(weather);
            restTemplate.postForObject(
                    Globals.SAVE_WEATHER_OBJECT,
                    weather,
                    Weather.class);
            currentRecordsInDb++;
        }
    }

    public void saveCityObjectToDatabase(CityWrapper cityWrapper, List<Weather> weathers) {
        City city = new City(
                cityWrapper.getCity_name(),
                cityWrapper.getCountry_code(),
                cityWrapper.getLon(),
                cityWrapper.getLat(),
                weathers);

        restTemplate.postForObject(
                Globals.SAVE_CITY_OBJECT,
                city,
                City.class);
    }
}
