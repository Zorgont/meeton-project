import React, { Component } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Chip from '@material-ui/core/Chip';
import DoneOutlinedIcon from '@material-ui/icons/DoneOutlined';
import CloseOutlinedIcon from '@material-ui/icons/CloseOutlined';
import { Link } from 'react-router-dom';
import { API_BASE_URL } from '../constants/constant';

class MeetingCardComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            lol: true
        }
    }

    render() {
        const meeting = this.props.meeting;
        const height = this.props.height ? this.props.height : "230px";
        console.log(this.props.request)

        return (
            <div className="container" style={{cursor: "pointer", height: height, borderRadius: "20px", backgroundImage: "linear-gradient(rgba(255,255,255,.8), rgba(255,255,255,.8)), url('https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80')", backgroundSize: "cover"}}>
                <Link to={`/users/${meeting.managerUsername}`} style={{textDecoration: "none", color: "black"}}>
                    <div className="row">
                        <div className="col-2 mt-2 justify-content-center"><Avatar src={API_BASE_URL + `/api/v1/users/${meeting.managerUsername}/avatar`}/></div>
                        <div className="col-10 mt-3">
                            <div>
                                <p>{this.props.meeting.managerUsername}</p>
                            </div>
                        </div>
                    </div>
                </Link>
                <Link to={`/meetings/${meeting.meetingId}`} style={{textDecoration: "none", color: "black"}}>
                    {this.props.request && <div className="row">
                        <div className="col text-center">
                            {this.props.request.status === "APPROVED" &&
                                <Chip icon={<DoneOutlinedIcon style={{color: "#569E68"}}/>} style={{backgroundColor: "#C1E6CD", color: "#569E68"}} label="Approved"/>
                            }
                            {this.props.request.status === "PENDING" &&
                                <Chip style={{backgroundColor: "#F9DDAB", color: "#111"}} label="Under consideration"/>
                            }
                            {this.props.request.status === "CANCELED" &&
                                <Chip icon={<CloseOutlinedIcon style={{color: "#fff"}}/>} style={{backgroundColor: "#D36347", color: "#fff"}} label="Rejected"/>
                            }
                            {/* <p className="text-center" style={{margin: "0"}}>Under consideration</p> */}
                        </div>
                    </div>}
                    <div className="row mt-2">
                        <div className="col-12">
                            <h3 className="text-center">{this.props.meeting.name}</h3>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-12">
                            <h5 className="text-center">{new Date(Date.parse(this.props.meeting.date + ':00+0000')).toUTCString().split(',')[1].split(' GMT')[0].slice(0, -3)}</h5>
                        </div>
                    </div>
                    <div className="row mt-1">
                        <div className="col text-center">
                        {
                            this.props.meeting.tags.map(tag => 
                                <Chip style={{backgroundColor: "#636363", color: "#fff"}} className="mt-1 ml-1 mr-1" label={tag}/>
                            )
                        }</div>
                    </div>
                </Link>
            </div>
        );
    }
}

export default MeetingCardComponent;