import React, { Component } from "react";
import MeetingService from "../services/MeetingService";
import 'font-awesome/css/font-awesome.min.css';
import AuthService from "../services/AuthService";
import RequestService from "../services/RequestService";
import CommentService from "../services/CommentService";
import CommentsList from "./CommentsListComponent";
import { Link } from 'react-router-dom';
import SockJsClient from "react-stomp";
import PlatformService from "../services/PlatformService";
import MeetingRating from "./MeetingRatingComponent";
import ScoreService from "../services/ScoreService";
import {Box} from "@material-ui/core";
import {Rating} from "@material-ui/lab";
import Badge from '@material-ui/core/Badge';
import Typography from '@material-ui/core/Typography';
import Chip from '@material-ui/core/Chip';
import DoneOutlinedIcon from '@material-ui/icons/DoneOutlined';
import CloseOutlinedIcon from '@material-ui/icons/CloseOutlined';
import Avatar from '@material-ui/core/Avatar';
import { API_BASE_URL } from '../constants/constant';

export default class NewMeetingPage extends Component{
    constructor(props) {
        super(props);

        this.state = {
            meeting: {},
            request: null,
            comments: [],
            currentUser: AuthService.getCurrentUser(),
            requestsAmount: 0,
            platforms: [],
            currentRating: 0,
            currentRatingCount: 0,
            userRating: 0, 
            loaded: false,
            participants: []
        };
    }

    componentDidMount() {
        MeetingService.getMeetingById(this.props.match.params.id).then((res) => {
            this.setState({ meeting: res.data});

            CommentService.getCommentsByMeetingId(this.state.meeting.meetingId).then((res) => {
                this.setState({ comments: res.data.reverse()});
                console.log(this.state.comments)
            });

            RequestService.getAprovedRequestsAmount(this.state.meeting.meetingId).then((res) => {
                this.setState({ requestsAmount: res.data});
            });

            RequestService.getRequestByMeetingAndUser(this.state.meeting.meetingId, this.state.currentUser?.id).then((res) => {
                this.setState({ request: res.data, loaded: true});
                console.log(this.state.request)
            });

            RequestService.getRequestsByMeetingId(this.state.meeting.meetingId).then((res) => {
                this.setState({ participants: res.data.filter(request => request.status === "APPROVED" && request.role !== "MANAGER")});
                console.log(this.state.participants)
            });

            PlatformService.getAllPlatforms().then(res => {
                this.setState({platforms: res.data});
                for(let platform of this.state.meeting.meetingPlatforms) {
                    let name = this.state.platforms.find(plat => plat.id === platform.platformId).name;
                    platform.name = name;
                    platform.meetingId = this.state.meeting.meetingId;
                }
                this.setState({meeting: this.state.meeting});
                console.log(this.state)
            });

            this.updateMeetingRating();
            ScoreService.getUserScore(this.state.meeting.meetingId, this.state.currentUser?.id).then( (res) => {
                this.setState({
                    userRating: res.data.score ? res.data.score : 0
                })
                console.log(this.state.userRating)
                }
            )

            console.log(`/meeting/${this.state.meeting.meetingId}/queue/comments`)
        });

    }

    updateMeetingRating = () => {
        ScoreService.getAggregatedScore(this.state.meeting.meetingId).then( (res) => {
                this.setState({
                    currentRating: res.data.score ? res.data.score : 0,
                    currentRatingCount: res.data.count ? res.data.count : 0
                })
            }
        )
    }

    commentsList(){
        if(this.state.meeting.status === "FINISHED" || this.state.meeting.status === "IN_PROGRESS") {
            return <div>
                <CommentsList comments={this.state.comments} onCommentChange={this.addComment.bind(this)}/>
                <SockJsClient
                    url={API_BASE_URL + `/ws`}
                    topics={[`/meeting/${this.state.meeting.meetingId}/queue/comments`]}
                    onMessage={(comment) => this.handleComment(comment)}
                    ref={ (client) => { this.clientRef = client }}/>
            </div>
        }
    }

    addComment(content){
        const comment = {
            meeting_id: this.state.meeting.meetingId,
            meetingName: this.state.meeting.name,
            user_id: this.state.currentUser.id,
            username: this.state.currentUser.username,
            content: content
        }
        this.clientRef.sendMessage("/app/createComment", JSON.stringify(comment));
    }

    handleComment(comment){
        console.log(comment)
        console.log(comment.date)
        this.state.comments.unshift(comment)
        this.setState({
            comments: this.state.comments
        })
    }

    deleteMeeting() {
        MeetingService.deleteMeeting(this.props.match.params.id).then((res) => {
            this.props.history.push('/meetings');
        });
    }

    buttonDelete() {
        if ((this.state.meeting.managerId === AuthService.getCurrentUser()?.id)&&(this.state.meeting.status!=="FINISHED"))
            return  <button className="btn btn-danger" style={{marginLeft:"5px"}} onClick={this.deleteMeeting.bind(this)}>Delete</button>
                    
    }

    buttonUpdate() {
        if ((this.state.meeting.managerId === AuthService.getCurrentUser()?.id)&&(this.state.meeting.status!=="FINISHED"))
            return <Link to={`/update/${this.props.match.params.id}`}>
                        <button className="btn btn-primary">Update</button>
                    </Link>
                    
    }

    handleEnroll() {
        if(this.state.meeting.isPrivate)
            this.props.history.push(`/enroll/${this.props.match.params.id}`)
        else
            this.sendEnrollment();
    }

    sendEnrollment() {
        let request = {
            user_id: this.state.currentUser.id,
            meeting_id:     this.state.meeting.meetingId
        };
        console.log(request)
        RequestService.createRequest(request).then(res => {
            this.props.history.push('/meetings');
        });
    }

    buttonEnroll() {
        if (AuthService.getCurrentUser()&&(this.state.meeting.managerId !== AuthService.getCurrentUser()?.id)&&(this.state.meeting.status!=="FINISHED")){
            if(this.state.request)
                return  ;
            if(this.state.meeting.isParticipantAmountRestricted && this.state.requestsAmount >= this.state.meeting.participantAmount)
                return <div><p>No available places!</p></div>

            if (this.state.loaded)
            return  <button className="btn btn-primary" style={{marginLeft:"5px"}} onClick={this.handleEnroll.bind(this)}>Enroll</button>
                   
        }
    }
     buttonRequests() {
        if((this.state.meeting.managerId === AuthService.getCurrentUser()?.id) && (this.state.meeting.status!=="FINISHED")){
            return <Link to={`/meetings/${this.props.match.params.id}/requests`}>
                    <button className="btn btn-primary" style={{marginLeft:"5px"}}>Requests</button>
                </Link>
        }
     }


    platformList() {
        if (this.state.request?.status === "APPROVED") {
            return  (
                <div>
                    <div className="row">Platforms </div>
                    {
                        this.state.meeting.meetingPlatforms?.map(
                            platform =>
                                <div className="row mb-2" style={{ background: "#dadada", borderRadius: "10px" }}>
                                    <div className="col-2"><img width="20px" height="20px" src="https://computercraft.ru/uploads/monthly_2018_09/discord_logo.0.jpg.7a69ad4c741ee1fb1bd39758714e7da5.jpg"></img></div>
                                    <div className="col-3">{platform.name}</div>
                                    <div className="col-6">{platform.address}</div>
                                </div>
                        )
                    }
                </div>
            );
        }
    }

    handleRatingChange = (value) => {
        const score={
            user_id: this.state.currentUser.id,
            score: parseInt(value)
        }
        ScoreService.setScore(this.state.meeting.meetingId, score).then( () =>
            this.updateMeetingRating()
        )
    }

    meetingRating = () => {
        console.log(this.state.currentUser);
        if(!(this.state.meeting.managerId === this.state.currentUser?.id ) && (this.state.meeting.status === "FINISHED") && (this.state.request?.status === "APPROVED" )) {
            return <MeetingRating onRatingChange={this.handleRatingChange.bind(this)} rating={this.state.userRating}/>
        }

    }

    render() {
        const { meeting, request, participants, currentRating, currentRatingCount } = this.state;
        console.log(meeting.date)
        let date = meeting.date !== undefined ? new Date(Date.parse(meeting.date + ':00+0000')).toUTCString().split(',')[1].split(' GMT')[0].slice(0, -3) : null
        let endDate = meeting.endDate !== undefined ? new Date(Date.parse(meeting.endDate + ':00+0000')).toUTCString().split(',')[1].split(' GMT')[0].slice(0, -3) : null
        return (
                <div className="container">
                    <div className="row">
                        <div className="col shadow-container">
                            <div className="row">
                                <div className="col text-center mt-4">
                                    {meeting !== null && meeting.isPrivate ?
                                        <Badge color="error" badgeContent="Private">
                                            <Typography><h1>{meeting.name}</h1></Typography>
                                        </Badge>
                                        : 
                                        <Badge color="primary" badgeContent="Public">
                                            <Typography><h1>{meeting.name}</h1></Typography>
                                        </Badge>
                                    }
                                </div>
                            </div>
                            {(meeting.status === "FINISHED") &&
                            <div className="row">
                                <div className="col-2 offset-5 text-center">
                                    <Box component="fieldset" mb={3} borderColor="transparent">
                                        <Rating name="read-only" size="large" value={currentRating}
                                                precision={0.1} readOnly/>
                                    </Box>
                                </div>
                                <div className="col-1">
                                    {currentRating.toFixed(1)} ({currentRatingCount})
                                </div>
                                <div className="col">
                                    {this.meetingRating()}
                                </div>
                            </div>
                            }
                            <div className="row">
                                <div className="col-4">
                                    <div className="row">
                                        <div className="col offset-2">
                                            <p>About meeting</p>
                                            <p>{meeting.about}</p>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-4 offset-2">
                                    <div className="row">
                                        <div className="col">
                                            <p>Beginning date</p>
                                            <p>{date}</p>
                                        </div>
                                        <div className="col">
                                            <p>End date</p>
                                            <p>{endDate}</p>
                                        </div>
                                    </div>
                                    <div className="row mt-2">
                                        <div className="col">
                                            <p>Manager</p>
                                        </div>
                                    </div>
                                    <Link to={`/users/${meeting.managerId}`} style={{textDecoration: "none", color: "black"}}>
                                        <div className="row">
                                            <div className="col-2"><Avatar src={`http://localhost:8080/api/v1/users/${meeting.managerId}/avatar`}/></div>
                                            <div className="col-9 mt-2" style={{marginLeft: "5px"}}>
                                                <div>
                                                    <p>{meeting.managerUsername}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </Link>
                                </div>
                            </div>
                            <div className="row mt-1">
                                <div className="col text-center">
                                {
                                    meeting?.tags?.map(tag => 
                                        <Chip style={{backgroundColor: "#636363", color: "#fff"}} className="mt-1 ml-1 mr-1" label={tag}/>
                                    )
                                }</div>
                            </div> 
                            <div className="row">
                                <div className="col text-center mt-2">
                                    {this.buttonUpdate()}
                                    {this.buttonDelete()}
                                    {this.buttonRequests()}
                                    {this.buttonEnroll()}
                                </div>
                            </div>
                            
                            <div className="row">
                                <div className="col-4">
                                    <div className="row">
                                        <div className="col offset-2 mb-2">
                                        {request?.status === "APPROVED" &&
                                        this.state.meeting.meetingPlatforms?.map(
                                            platform =>  <div className="row mt-1">
                                                <div className="col">
                                                    {/* <div style={{backgroundColor: "#ccc", width: "50px", height: "50px", borderRadius: "5px", display: "inline-block"}}>
                                                    <img style={{marginLeft: "5px", marginTop: "5px", height: "40px", width: "40px"}} src="https://e7.pngegg.com/pngimages/364/717/png-clipart-zoom-video-communications-apptrailers-mobile-phones-android-blue-cloud-computing.png"/>
                                                    </div> */}
                                                    <a className="platform-link" href={platform.address}>{platform.name}</a>
                                                </div>
                                                </div>
                                        )
                                    }
                                        </div>
                                    </div>
                                </div>
                                <div className="col offset-2 mt-4 mb-2">
                                    {(AuthService.getCurrentUser()&&(this.state.meeting.managerId !== AuthService.getCurrentUser()?.id) && request) &&
                                        <div className="row">
                                            <div className="col">
                                                <span style={{marginRight: "10px"}}>Request status</span>
                                                {request.status === "APPROVED" &&
                                                <Chip icon={<DoneOutlinedIcon style={{color: "#569E68"}}/>} style={{backgroundColor: "#C1E6CD", color: "#569E68"}} label="Approved"/>
                                            }
                                            {request.status === "PENDING" &&
                                                <Chip style={{backgroundColor: "#F9DDAB", color: "#111"}} label="Under consideration"/>
                                            }
                                            {request.status === "CANCELED" &&
                                                <Chip icon={<CloseOutlinedIcon style={{color: "#fff"}}/>} style={{backgroundColor: "#D36347", color: "#fff"}} label="Rejected"/>
                                            }
                                            </div>
                                        </div>
                                    }
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="row">
                        {/* comments */}{(this.state.meeting.status === "FINISHED" || this.state.meeting.status === "IN_PROGRESS") &&
                        <div className="col-9 shadow-container mt-2 mb-4">
                            <div className="row">
                                
                                <div className="col">
                                    <div className="row">
                                        <div className="col text-center mt-2">
                                            <h2>Comments</h2>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col mb-2">
                                            {this.commentsList()}
                                        </div>
                                    </div>
                                </div>
                            </div>
                                
                        </div>}
                        {/* participants */}
                        <div className="col shadow-container mt-2 mb-4">
                            <div className="row">
                                <div className="col">
                                <div className="row">
                                    <div className="col text-center mt-2">
                                        <h2>Participants</h2>
                                    </div>
                                    </div>
                                    <div className="row">
                                        <div className="col mb-2">
                                            {participants?.map(participant => 
                                                <Link to={`/users/${participant.user_id}`} style={{textDecoration: "none", color: "black"}}>
                                                <div className="row">
                                                    <div className="col-2"><Avatar src={`http://localhost:8080/api/v1/users/${participant.user_id}/avatar`}/></div>
                                                    <div className="col-9 mt-2" style={{marginLeft: "5px"}}>
                                                        <div>
                                                            <p>{participant.username}</p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </Link>
                                            )
                                            }
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        );
    }
}