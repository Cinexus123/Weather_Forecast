import axios from 'axios'

const CITY_REST_API_URL = "http://localhost:5001/city";
const WEATHER_REST_API_URL = "http://localhost:5001/weather";

class CityService {

    getCities() {
       return axios.get(CITY_REST_API_URL);
    }

    createCity(name, country_code) {
        return axios.post(CITY_REST_API_URL + '/addNewCity/' + name + '/' + country_code);
    }

    deleteCity(name) {
        return axios.delete(CITY_REST_API_URL + '/' + name);
    }

    getBestWeather(date) {
        return axios.get(WEATHER_REST_API_URL + '/' + date);
    }
}

export default new CityService();
