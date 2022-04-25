import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import TextField from '@material-ui/core/TextField';

import { Link } from 'react-router-dom';
import CheckButton from "react-validation/build/button";
import GoogleLogin from "react-google-login";

import AuthService from "../services/AuthService";
import UserService from "../services/UserService";
import { GITHUB_AUTH_URL, GOOGLE, GOOGLE_AUTH_URL } from "../constants/constant";

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

export default class NewLoginComponent extends Component {
    constructor(props) {
        super(props);
        this.handleLogin = this.handleLogin.bind(this);
        this.onChangeUsername = this.onChangeUsername.bind(this);
        this.onChangePassword = this.onChangePassword.bind(this);

        this.state = {
            username: "",
            password: "",
            loading: false,
            message: ""
        };
    }

    onChangeUsername(e) {
        this.setState({
            username: e.target.value
        });
    }

    onChangePassword(e) {
        this.setState({
            password: e.target.value
        });
    }

    handleLogin(e) {
        e.preventDefault();

        this.setState({
            message: "",
            loading: true
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            AuthService.login(this.state.username, this.state.password).then(
                () => {
                    this.props.history.push("/profile");
                    window.location.reload();
                },
                error => {
                    console.log(error.response)
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                    this.setState({
                        loading: false,
                        message: resMessage
                    });
                }
            );
        } else {
            this.setState({
                loading: false
            });
        }
    }

    onSuccessfulGoogleAuthorization = (response) => {
        console.log(response);
        console.log("hell yeah");
        let user = {
            email: response.profileObj.email,
            firstName: response.profileObj.givenName,
            secondName: response.profileObj.familyName,
            accessToken: response.accessToken
        }
        console.log(`/usernameInput?email=${user.email}&firstName=${user.firstName}&secondName=${user.secondName}&accessToken=${user.accessToken}`)

        AuthService.existsUserByEmail(user.email).then(res => {
            console.log(res.data);
            if (res.data) {
                AuthService.loginViaGoogle(user).then(res => {
                    this.props.history.push("/profile")
                    window.location.reload();
                }, error => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                    this.setState({
                        loading: false,
                        message: resMessage
                    })
                });
            }
            else {
                this.props.history.push(`/usernameInput?email=${user.email}&firstName=${user.firstName}&secondName=${user.secondName}&accessToken=${user.accessToken}`);
                window.location.reload();
            }
        })
    }

    onFailureGoogleAuthorization = (response) => {
        console.log(response)
        console.log("damn it")
        this.setState({
            message: response.error
        })
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-6 offset-3">
                        <div className="row">
                            <div className="col">
                                <h1 className="text-center raleway custom-header">Login</h1>
                            </div>
                        </div>
                        <div className="row mt-1 mb-3">
                            <div className="col">
                                <p className="text-center raleway custom-paragraph">Welcome back</p>
                            </div>
                        </div>
                        <div className="row mb-4">
                            <div className="col">
                                {/* <GoogleLogin
                                    clientId="9387373968-sqc6916e9o8h2usu5p981bdaj4sr4lu9.apps.googleusercontent.com"
                                    onSuccess={this.onSuccessfulGoogleAuthorization}
                                    onFailure={this.onFailureGoogleAuthorization}
                                    cookiePolicy={'single_host_origin'}
                                    render = {renderProps => {
                                        return <button className="btn btn-danger raleway custom-paragraph" onClick={renderProps.onClick} style={{width: "100%", backgroundColor: "#CD5642", marginLeft: "8px", marginRight: "-8px"}}><i class="fa fa-google" style={{color: "white", marginRight: "10px"}}/>Join using Google</button>
                                    }}/> */}
                                    <div>
                                        <a className="btn btn-danger raleway custom-paragraph" href={GOOGLE_AUTH_URL} style={{width: "100%", backgroundColor: "#CD5642", marginLeft: "8px", marginRight: "-8px"}}><i class="fa fa-google" style={{color: "white", marginRight: "10px"}}/>Join using Google</a>
                                    </div>
                            </div>
                        </div>
                        <div className="row mb-4">
                            <div className="col">
                                {/* <GoogleLogin
                                    clientId="9387373968-sqc6916e9o8h2usu5p981bdaj4sr4lu9.apps.googleusercontent.com"
                                    onSuccess={this.onSuccessfulGoogleAuthorization}
                                    onFailure={this.onFailureGoogleAuthorization}
                                    cookiePolicy={'single_host_origin'}
                                    render = {renderProps => {
                                        return <button className="btn btn-danger raleway custom-paragraph" onClick={renderProps.onClick} style={{width: "100%", backgroundColor: "#CD5642", marginLeft: "8px", marginRight: "-8px"}}><i class="fa fa-google" style={{color: "white", marginRight: "10px"}}/>Join using Google</button>
                                    }}/> */}
                                    <div>
                                        <a className="btn raleway custom-paragraph" href={GITHUB_AUTH_URL} style={{width: "100%", backgroundColor: "#666", marginLeft: "8px", marginRight: "-8px", color: "white"}}><i class="fa fa-github" style={{color: "white", marginRight: "10px"}}/>Join using GitHub</a>
                                    </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col">
                                <p className="text-center raleway custom-paragraph">OR</p>
                            </div>
                        </div>

                        <Form onSubmit={this.handleLogin} ref={c => { this.form = c; }}>
                            <div className="row">
                                <div className="col">
                                    <p className="raleway custom-paragraph" style={{marginLeft: "9px", marginBottom: "0"}}>Username</p>
                                    <TextField style={{ margin: "8px" }} placeholder="Enter your username" required
                                    fullWidth margin="normal" variant="outlined" onChange={this.onChangeUsername}
                                    InputLabelProps={{
                                        shrink: true,
                                    }}/>
                                </div>
                            </div>
                            <div className="row mt-1">
                                <div className="col">
                                    <p className="raleway custom-paragraph" style={{marginLeft: "9px", marginBottom: "0"}}>Password</p>
                                    <p className="underlined-link" style={{marginBottom: "0", position: "absolute", right: "9px", top: "0"}}>Forgot your password?</p>
                                    <TextField style={{ margin: "8px" }} placeholder="Enter your password" type="password" required
                                    fullWidth margin="normal" variant="outlined" onChange={this.onChangePassword}
                                    InputLabelProps={{
                                        shrink: true,
                                    }}/>
                                </div>
                            </div>
                            <div className="row mt-3 mb-2">
                                <div className="col">
                                    <button className="btn btn-primary raleway custom-paragraph" style={{width: "100%", backgroundColor: "#373737", marginLeft: "8px", marginRight: "-8px"}} type="submit">Login</button>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col">
                                {this.state.message && (
                                    <div className="alert alert-danger" role="alert" style={{marginLeft: "8px", marginRight: "-8px"}}>
                                        {this.state.message}
                                    </div>
                                )}
                                </div>
                            </div>
                            <div className="row">
                                <div className="col">
                                    <p className="text-center raleway custom-paragraph">Don't have an account? <Link className="underlined-link" to="/register">Join</Link></p>
                                </div>
                            </div>
                            <CheckButton style={{ display: "none" }} ref={c => { this.checkBtn = c; }} />
                        </Form>
                    </div>
                </div>
                
            </div>
        );
    }
}