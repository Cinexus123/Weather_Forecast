import React, {Component} from 'react';
import CityService from "../services/CityService";

class CreateCityComponent extends Component {

    constructor(props) {
        super(props);

        this.state = {
          name: '',
            country_code:'',
            lon:'',
            lat:''
        }
        this.changeNameHandler = this.changeNameHandler.bind(this);
        this.changeCountryCodeHandler = this.changeCountryCodeHandler.bind(this);
        this.addNewCity = this.addNewCity.bind(this);
    }

    addNewCity = (e) => {
        e.preventDefault();

        let city = {name: this.state.name,country_code: this.state.country_code,lon: this.state.lon,lat: this.state.lat};
        console.log('city => ' + JSON.stringify(city));

        CityService.createCity(city.name,city.country_code).then(res => {
           this.props.history.push('/');
        });
    }

    changeNameHandler = (event) =>{
        this.setState({name: event.target.value});
    }

    changeCountryCodeHandler = (event) =>{
        this.setState({country_code: event.target.value});
    }

    cancel() {
        this.props.history.push('/');
    }
    render() {
        return (
            <div>
                <div className="container">
                    <div className="row">
                     <div className="card col-md-6 offset-md-3 offset-md-3">
                         <h3 className="text-center"> Add City</h3>
                         <div className="card-body">
                             <form>
                                 <div className="form-group">
                                     <label> Name:</label>
                                     <input placeholder="Name" name="name" className="form-control"
                                            value={this.state.name} onChange={this.changeNameHandler}/>
                                 </div>
                                 <div className="form-group">
                                     <label> Country code:</label>
                                     <input placeholder="Country code" name="country_code" className="form-control"
                                            value={this.state.country_code} onChange={this.changeCountryCodeHandler}/>
                                 </div>
                                     <button className ="btn btn-success" onClick={this.addNewCity}>Save</button>
                                     <button className="btn btn-danger" onClick={this.cancel.bind(this)} style={{marginLeft: "10px"}}>Cancel</button>
                             </form>

                         </div>
                     </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default CreateCityComponent;
