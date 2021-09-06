import './App.css';
import CityComponent from "./components/CityComponent";
import HeaderComponent from "./components/HeaderComponent";
import FooterComponent from "./components/FooterComponent";
import CreateCityComponent from "./components/CreateCityComponent";
import BestWeatherComponent from "./components/BestWeatherComponent";
import {BrowserRouter as Router, Route,Switch} from 'react-router-dom'
function App() {
  return (
      <div>
          <Router>
                  <HeaderComponent/>
                  <div className="container">
                      <Switch>
                          <Route path = "/" exact component = {CityComponent}></Route>
                          <Route path = "/city" component = {CityComponent}></Route>
                          <Route path = "/addNewCity" component = {CreateCityComponent}></Route>
                          <Route path = "/bestWeather" component = {BestWeatherComponent}></Route>
                        {/*  <CityComponent/>*/}
                      </Switch>
                  </div>
                  <FooterComponent/>
          </Router>
      </div>
  );
}

export default App;
