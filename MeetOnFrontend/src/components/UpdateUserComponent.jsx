import React, { Component } from 'react';
import UserService from '../services/UserService';

class UpdateUserComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            firstName: '',
            about: ''
        }
    }

    updateUser = (event) => {
        event.preventDefault();
        let user = { firstName: this.state.firstName, about: this.state.about };
        UserService.updateUser(user, this.state.id).then(res => {
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

    componentDidMount() {
        UserService.getUserById(this.state.id).then(res => {
            let user = res.data;
            this.setState({firstName: user.firstName, about: user.about});
        });
    }

    render() {
        return (
            <div>
                <div className="container">
                    <div className="row">
                        <div className="card col-md-6 offset-md-3 offset-md-3">
                            <h3 className="text-center">Update User</h3>
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

                                    <button className="btn btn-success" onClick={this.updateUser.bind(this)}>Save</button>
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

export default UpdateUserComponent;