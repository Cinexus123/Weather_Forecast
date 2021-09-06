import React, {Component} from 'react';

class HeaderComponent extends Component {

    constructor(props) {
        super();
        this.state = {

        }
    }

    render() {
        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                        <div><a href = "https://javaguides.net" className="navbar-brand">Weather Application</a> </div>
                    </nav>
                </header>
            </div>
        );
    }
}

export default HeaderComponent;
