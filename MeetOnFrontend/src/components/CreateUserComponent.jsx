import React, { Component } from 'react';
import UserService from '../services/UserService';

class CreateUserComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            firstName: '',
            about: ''
        }
    }

    saveUser = (event) => {
        event.preventDefault();
        let user = { firstName: this.state.firstName, about: this.state.about };
        UserService.createUser(user).then(res => {
            this.props.history.push('/users');
        });
    }

    changeNameHandler = (event) => {
        this.setState({firstName: event.target.value});
    }

    changeAboutHandler = (event) => {
        this.setState({about: event.target.value});
    }

    cancel() {
        this.props.history.push('/users');
    }

    render() {
        return (
            <div>
                <div className="container">
                    <div className="row">
                        <div className="card col-md-6 offset-md-3 offset-md-3">
                            <h3 className="text-center">Add User</h3>
                            <div className="card-body">
                                <form>
                                    <div className="form-group">
                                        <label for="name"> First Name: </label>
                                        <input type="text" name="name" className="form-control" value={this.state.firstName} onChange={this.changeNameHandler.bind(this)} required />
                                    </div>
                                    <div className="form-group">
                                        <label for="about"> About: </label>
                                        <input type="text" name="about" className="form-control" value={this.state.about} onChange={this.changeAboutHandler.bind(this)} required />
                                    </div>

                                    <button className="btn btn-success" onClick={this.saveUser.bind(this)}>Save</button>
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

export default CreateUserComponent;