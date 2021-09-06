import React from 'react';
import UserService from "../services/CityService";
import CityService from "../services/CityService";
import 'bootstrap/dist/css/bootstrap.min.css'

class CityComponent extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            city:[]
        }
        this.addNewCity = this.addNewCity.bind(this);
        this.deleteCity = this.deleteCity.bind(this);
        this.calculateBestWeather = this.calculateBestWeather.bind(this);
    }

    componentDidMount() {
        UserService.getCities().then((response) => {
            this.setState({city: response.data})
        });
    }

    addNewCity() {
        console.log(this.props);
        this.props.history.push('/addNewCity');
    }

    calculateBestWeather() {
        this.props.history.push('/bestWeather');
    }


    deleteCity(name) {
      CityService.deleteCity(name).then(res => {
          this.setState({city: this.state.city.filter(city => city.name !== name)});
      })
    }

    render() {
        return (
            <div>
                <h1 className="text-center">City list</h1>
                <div className= "row">
                <table className="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <td><b>City name</b></td>
                        <td><b>City country code</b></td>
                        <td><b>City lon</b> </td>
                        <td><b>City lat</b></td>
                        <td>Actions</td>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.city.map(
                            city =>
                                <tr key = {city.name}>
                                    <td> {city.name}</td>
                                    <td> {city.country_code}</td>
                                    <td> {city.lon}</td>
                                    <td> {city.lat}</td>
                                    <td>
                                        <button onClick={ () => this.deleteCity(city.name)} className="btn btn-danger">Delete </button>
                                    </td>
                                </tr>
                        )
                    }
                    </tbody>
                </table>
                </div>
                <button className="btn btn-primary" onClick={this.addNewCity}> Add new city</button>
                <h1>
                    <button className="btn btn-info" onClick={this.calculateBestWeather}> Get best weather city</button>
                </h1>
            </div>
        )
    }

}

export default CityComponent
