import React, { Component } from "react";
import AuthService from "../services/AuthService";
import MeetingService from "../services/MeetingService";
import 'font-awesome/css/font-awesome.min.css';
import RequestService from "../services/RequestService";

export default class CreateMeeting extends Component{
    constructor(props) {
        super(props);
        this.state = {
            currentUserId: AuthService.getCurrentUser().id,
            about: "",
            meeting: null,
            loaded: false
        };  
        
    }
    enroll= (event) => {
        event.preventDefault();
        this.sendEnrollment();
    }
    componentDidMount(){
        MeetingService.getMeetingById(this.props.match.params.id).then(res => {
            this.setState({meeting: res.data, loaded: true});
        });
    }

    sendEnrollment() {
        if (!this.state.loaded)
            return;

        let request = {
            user_id: this.state.currentUserId,
            about: this.state.about,
            meeting_id:this.state.meeting.meetingId    
        };
        console.log(request)
        RequestService.createRequest(request).then(res => {
            this.props.history.push('/meetings');
        });
    }
  
    changeAboutHandler = (event) => {
        this.setState({about: event.target.value});
        console.log(event.target.value);
    }
    
    cancel() {
        this.props.history.push('/meetings');
    }
    render() {
        return (
            <div>   
                <div className="container">
                    <div className="row">
                        <div className="card col-md-6 offset-md-3 offset-md-3">
                            <h3 className="text-center">Enroll on the meeting</h3>
                            <div className="card-body">
                                <form>
                                    <div className="form-group row">
                                        <label for="about"> About: </label>
                                        <input type="text" name="about" className="form-control" value={this.state.about} onChange={this.changeAboutHandler.bind(this)} />
                                    </div>
                                    <div className="row">
                                        <button className="btn btn-success" onClick={this.enroll.bind(this)}>Enroll</button>
                                        <button className="btn btn-danger" onClick={this.cancel.bind(this)} style={{marginLeft: "10px"}}>Cancel</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}