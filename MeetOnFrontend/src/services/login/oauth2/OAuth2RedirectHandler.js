import React, { Component } from 'react';
import { ACCESS_TOKEN } from '../../../constants/constant';
import { Redirect } from 'react-router-dom'
import AuthService from '../../AuthService';
import UserService from '../../UserService';

class OAuth2RedirectHandler extends Component {
    getUrlParameter(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');

        var results = regex.exec(this.props.location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    render() {        
        const token = this.getUrlParameter('token');
        const error = this.getUrlParameter('error');

        if(token) {
            localStorage.setItem(ACCESS_TOKEN, token);
            AuthService.getNewCurrentUser().then(
                response => { 
                    console.log(response.data)
                    if (response.data.valid) {
                        console.log("BEFORE redirecting to profile...");
                        UserService.getUserByUsername(response.data.username).then(res => {
                            res.data.token = localStorage.getItem(ACCESS_TOKEN);
                            localStorage.setItem("user", JSON.stringify(res.data));

                            console.log("redirecting to profile...");
                            this.props.history.push(`/profile`);
                            window.location.reload();
                        });
                    } else {
                        console.log("redirecting to username input page...");
                        this.props.history.push(`/usernameInput?email=${response.data.email}&firstName=${response.data.firstName}&secondName=${response.data.secondName}`);
                        window.location.reload();
                    }
                },
                error => {
                    console.log(error);
                }
            );
        } else {
            return <Redirect to={{
                pathname: "/login",
                state: { 
                    from: this.props.location,
                    error: error 
                }
            }}/>; 
        }
    }
}

export default OAuth2RedirectHandler;