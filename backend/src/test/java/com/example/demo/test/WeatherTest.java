package com.example.demo.test;

import com.example.demo.entities.City;
import com.example.demo.entities.Weather;
import com.example.demo.services.impl.WeatherServiceImpl;
import com.example.demo.wrappers.CityWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.demo.config.properties.Globals.TEST_URL;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherTest {

    @LocalServerPort
    int port;

    @Autowired
    WeatherServiceImpl weatherServiceImpl;

    private RestTemplate restTemplate = new RestTemplate();

    URI getCityUri(String path) throws URISyntaxException {
        return new URI("http://localhost:" + port + "/city" + path);
    }

    URI getWeatherUri(String path) throws URISyntaxException {
        return new URI("http://localhost:" + port + "/weather" + path);
    }

    //////////////CITY TEST//////////////////
    @Test
    void checkInputDataInDatabaseForCityTable() throws URISyntaxException {

        ParameterizedTypeReference<List<City>> responseType = new ParameterizedTypeReference<>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<City>> resp = restTemplate.exchange(getCityUri(""), HttpMethod.GET, entity, responseType);
        List<City> list = resp.getBody();

        assertEquals(list.size(), 5);
        assertEquals(list.get(0).getName(), "Bridgetown");
        assertFalse(list.get(0).getName().contains("Szczecin"));
    }

    @Test
    void addNewObjectToDatabase() throws URISyntaxException {

        ParameterizedTypeReference<List<City>> responseType = new ParameterizedTypeReference<>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        restTemplate.postForEntity(getCityUri("/addNewCity/Warszawa/PL"), HttpMethod.POST, City.class);

        ResponseEntity<List<City>> resp = restTemplate.exchange(getCityUri(""), HttpMethod.GET, entity, responseType);
        List<City> list = resp.getBody();

        assertEquals(list.size(), 6);
        assertEquals(list.get(5).getName(), "Warszawa");
        assertEquals(list.get(5).getCountry_code(), "PL");
    }

    @Test
    void tryToAddCityToLocalDbWhichDoesntAppearInAPI() {

        ResponseEntity<City> statusCityNotInAPI = null;
        ResponseEntity<City> statusCityInAPI = null;
        try {
            statusCityInAPI = restTemplate.postForEntity(getCityUri("/addNewCity/Bialystok/PL"), HttpMethod.POST, City.class);
            statusCityNotInAPI = restTemplate.postForEntity(getCityUri("/addNewCity/Białystok/PL"), HttpMethod.POST, City.class);
        } catch (Exception e) {
            System.out.println("Nie udało się wyciągnąć takiej miejscowości z bazy");
        }

        assertNull(statusCityNotInAPI);
        assertNotNull(statusCityInAPI);
    }

    @Test
    void checkRemovedElementsInDatabase() throws URISyntaxException {

        ParameterizedTypeReference<List<City>> responseType = new ParameterizedTypeReference<>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<City>> resp = restTemplate.exchange(getCityUri(""), HttpMethod.GET, entity, responseType);
        List<City> list = resp.getBody();

        restTemplate.delete(getCityUri("/Jastarnia").toString(), HttpMethod.DELETE, entity, responseType);

        ResponseEntity<List<City>> respAfterDelete = restTemplate.exchange(getCityUri(""), HttpMethod.GET, entity, responseType);
        List<City> listAfterDelete = respAfterDelete.getBody();

        assertEquals(list.size(), 5);
        assertEquals(listAfterDelete.size(), 4);
    }

    @Test
    void checkCityIsCorrectCreateByStatusCode() throws URISyntaxException {

        ParameterizedTypeReference<List<City>> responseType = new ParameterizedTypeReference<>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        CityWrapper cityWrapper = restTemplate.getForObject(TEST_URL, CityWrapper.class);
        weatherServiceImpl.saveCityObjectToDatabase(cityWrapper, null);

        ResponseEntity<List<City>> resp = restTemplate.exchange(getCityUri(""), HttpMethod.GET, entity, responseType);

        assertFalse(resp.getStatusCode().toString().contains("404"));
        assertTrue(resp.getStatusCode().toString().contains("200"));
    }

    @Test
    void checkBestPlaceForSurfingMethod() {

        List<Weather> testList = new ArrayList<>();
        LocalDate localDate = LocalDate.parse("2021-09-11");
        String cityForSurfing = null;
        String expectedCity = "Kielce";
        String notExpectedCity = "Jastarnia";

        Weather weather = new Weather(
                1L,
                15.2,
                13.3,
                localDate,
                expectedCity);


        Weather weather1 = new Weather(
                1L,
                3.2,
                16.7,
                localDate,
                notExpectedCity);

        testList.add(weather);
        testList.add(weather1);

        for (int weatherId = 0; weatherId < testList.size(); weatherId++) {
            if (testList.get(weatherId).getTemp() > 5.0 && testList.get(weatherId).getTemp() < 35.0) {
                cityForSurfing = testList.get(weatherId).getCity();
            }
        }
        assertTrue(expectedCity.contains(cityForSurfing));
        assertFalse(notExpectedCity.contains(cityForSurfing));
    }
    ////////////////////////WEATHER TEST///////////////////////////

    @Test
    void checkRecordsMassDeleteFromWeatherTable() throws URISyntaxException {

        ParameterizedTypeReference<List<Weather>> responseType = new ParameterizedTypeReference<>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        LocalDate localDate = LocalDate.parse("2021-09-11");

        Weather weather = new Weather(
                1L,
                12.2,
                13.3,
                localDate,
                "Szczecin");

        restTemplate.postForObject(
                getWeatherUri("/saveWeatherObject"),
                weather,
                Weather.class);

        Weather weather1 = new Weather(
                2L,
                12.2,
                13.3,
                localDate,
                "Stargard");

        restTemplate.postForObject(
                getWeatherUri("/saveWeatherObject"),
                weather1,
                Weather.class);

        Weather weather2 = new Weather(
                3L,
                12.2,
                13.3,
                localDate,
                "Szczecin");

        restTemplate.postForObject(
                getWeatherUri("/saveWeatherObject"),
                weather2,
                Weather.class);

        weatherServiceImpl.getWeatherAndDelete("Szczecin");

        ResponseEntity<List<Weather>> resp = restTemplate.exchange(getWeatherUri(""), HttpMethod.GET, entity, responseType);

        List<Weather> testList = resp.getBody();

        assertEquals(testList.size(), 1);
        assertFalse(testList.get(0).getCity().contains("Szczecin"));
    }

    @Test
    void checkInputDataInDatabaseForWeatherTable() throws URISyntaxException {

        ParameterizedTypeReference<List<Weather>> responseType = new ParameterizedTypeReference<>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        LocalDate localDate = LocalDate.parse("2021-09-11");

        Weather weather = new Weather(
                1L,
                12.2,
                13.3,
                localDate,
                "Szczecin");

        restTemplate.postForObject(
                getWeatherUri("/saveWeatherObject"),
                weather,
                Weather.class);

        ResponseEntity<List<Weather>> resp = restTemplate.exchange(getWeatherUri(""), HttpMethod.GET, entity, responseType);

        List<Weather> testList = resp.getBody();

        assertEquals(testList.size(), 1);
        assertNotEquals(testList.get(0).getWind_spd(), 11.0);
    }

    @Test
    void getListOfWeathersForCity() {

        List<Weather> weather = weatherServiceImpl.getListOfWeatherForCity("Szczecin", "PL");

        assertEquals(weather.size(), 16);
        assertFalse(weather.get(10).getCity().equals("Wroclaw"));
    }


    @Test
    void checkDateFormatForSurfingMethod() {

        String localDateCorrectTemplate = "2021-09-11";
        String localDateBadTemplate = "11.09.2021";
        Date correctDate = null;
        Date badDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try {
            correctDate = format.parse(localDateCorrectTemplate);
            badDate = format.parse(localDateBadTemplate);
        } catch (ParseException e) {
            System.out.println("Błąd parsowania daty");
        }

        assertNotNull(correctDate);
        assertNull(badDate);
    }
}
