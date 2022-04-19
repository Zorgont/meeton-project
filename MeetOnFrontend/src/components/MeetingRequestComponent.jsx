import React, { Component } from 'react';
import RequestService from '../services/RequestService';
import UserService from '../services/UserService';

export default class MeetingRequest extends Component {
    constructor(props) {
        super(props);
        this.state = {
            meetingId: this.props.match.params.id,
            requests: []
        }
    }
    
    componentDidMount() {
        RequestService.getPendingRequests(this.state.meetingId).then((res) => {
            this.setState({ requests: res.data });
        });
    }

    changeStatus(request, status) {
        console.log(request);
        console.log(request + " " + status);
        RequestService.updateRequestStatus(request.id, status).then((res) => {
            this.state.requests.splice(this.state.requests.indexOf(request),1)
            this.setState({requests: this.state.requests});
        });
    }

    render() {
        return (
            <div>
                <h2 className="text-center">Requests List</h2>
                <div className="row">
                    <table className="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>User Name</th>
                                <th>Description</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        {
                            this.state.requests.map(
                                request => 
                                <tr key={request.id}>
                                    <td> {request.username} </td>
                                    <td> {request.about} </td>
                                    <td> {request.status} </td>
                                    <td> 
                                        <button onClick={() => this.changeStatus(request, "APPROVED")} className="btn btn-success">Approve</button>
                                        <button onClick={() => this.changeStatus(request, "CANCELED")} className="btn btn-danger" style={{marginLeft: "10px"}}>Deny</button>
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