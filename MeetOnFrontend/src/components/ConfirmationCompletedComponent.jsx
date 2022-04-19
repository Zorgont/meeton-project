import React, {Component} from "react";
import AuthService from "../services/AuthService";

export default class ConfirmationCompleted extends Component{
    constructor(props) {
        super(props);
        this.state={
            result:""
        }
    }
    componentDidMount() {
        const params = new URLSearchParams (this.props.location.search);
        const token = params.get('token');
        AuthService.confirmUser(token).then(res => {
            this.setState({
                result: res.data
            })
        })
    }
    render() {
        return <div>
            <h6>{this.state.result}</h6>
        </div>
    }
}