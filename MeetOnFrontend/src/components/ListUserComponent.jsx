import React, { Component } from 'react';
import UserService from '../services/UserService';

class ListUserComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            users: []
        }
        this.addUser = this.addUser.bind(this);
        this.editUser = this.editUser.bind(this);
        this.deleteUser = this.deleteUser.bind(this);
    }
    
    componentDidMount() {
        UserService.getUsers().then((res) => {
            this.setState({users: res.data});
        });
    }

    addUser() {
        this.props.history.push('/add_user');
    }

    editUser(id) {
        this.props.history.push(`/update_user/${id}`)
    }

    deleteUser(id) {
        UserService.deleteUser(id).then(res => {
            this.setState({users: this.state.users.filter(user => user.id !== id)});
        });
    }

    render() {
        return (
            <div>
                <h2 className="text-center">Users List</h2>
                <div className="row">
                    <button className="btn btn-primary" onClick={this.addUser}>Add User</button>
                </div>
                <div className="row">
                    <table className="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>User Name</th>
                                <th>User About</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.users.map(
                                    user => 
                                    <tr key={user.id}>
                                        <td> {user.firstName} </td>
                                        <td> {user.about} </td>
                                        <td> 
                                            <button onClick={() => this.editUser(user.id)} className="btn btn-info">Update</button>
                                            <button onClick={() => this.deleteUser(user.id)} className="btn btn-danger" style={{marginLeft: "10px"}}>Delete</button>
                                        </td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default ListUserComponent;