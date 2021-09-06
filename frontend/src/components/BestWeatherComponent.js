import React, {Component, useState} from 'react';
import CityService from "../services/CityService";
import Calendar from "react-calendar";
import '@natscale/react-calendar/dist/main.css';


const ReactCalendar = () => {
    const [date,setDate] = useState(new Date());

    const onChange = date => {
        setDate(date);
    };

    return (
        <div>
            <Calendar onChange={onChange} value={date} />
            <div id="calendar-value">{new Intl.DateTimeFormat('fr-CA').format(date).toString()}</div>
        </div>
    );
}

class BestWeatherComponent extends Component {

    constructor(props) {
        super(props);

        this.state = {
            date: new Date(),
            city:[]
        };
    }

    calculateBestWeather(date) {
        CityService.getBestWeather(date).then((response) => {
            console.log(response)
            this.setState({city: response.data})

        });
    }

    render() {
        console.log("Date1: " + ReactCalendar.date);
        return (
            <div>
                <ReactCalendar/>
                <h1>
                    <button className="btn btn-dark" onClick={this.calculateBestWeather()}> Calculate weather</button>
                </h1>
            </div>
        );
    }
}

export default BestWeatherComponent;
