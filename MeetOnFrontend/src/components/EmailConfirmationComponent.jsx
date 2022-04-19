import React, {Component} from "react";
import {Link} from "react-router-dom";

export default class EmailConfirmation extends Component{
    constructor(props) {
        super(props);
        this.state={
            mail:""
        }
    }
    componentDidMount() {
        const params = new URLSearchParams (this.props.location.search);
        this.setState({
            mail: params.get('email')
        })
    }
    render() {
        return <div>
            <div className="row">
                <h6>A confirmation mail was send to your address {this.state.mail} . To complete the registration, check it,please.</h6>
            </div>
            <div className="row">
                <div className="form-group">
                    <Link to={'/login'}>
                        <button className="btn btn-primary btn-block">Login</button>
                    </Link>
                </div>
            </div>
        </div>
    }
}