import React, { Component } from "react";
import AuthService from "../services/AuthService";
import MeetingService from "../services/MeetingService";
import 'font-awesome/css/font-awesome.min.css';
import PlatformService from "../services/PlatformService";
import {InputLabel, TextField} from "@material-ui/core";

export default class CreateMeeting extends Component{
    constructor(props) {
        super(props);
        this.state = {
            currentUser: AuthService.getCurrentUser(),
            name: "",
            about: "",
            date: "",
            endDate:"",
            isParticipantAmountRestricted: false,
            participantAmount: 0,
            isPrivate: false,
            details: "",
            tags: [],
            meetingPlatforms: [],
            platforms: [],
            selectedMeetingPlatform: null,
            selectedMeetingPlatformAddress: null,
            isOpenNewPlatformOpened: false,
            fields: "",
            errorMsg: "",
            errors: []
        };
    }

    componentDidMount() {
        MeetingService.getMeetingById(this.props.match.params.id).then(res => {
            let meeting = res.data;
            this.setState(
                {
                    name: meeting.name,
                    about: meeting.about,
                    date: meeting.date,
                    endDate:meeting.endDate,
                    isParticipantAmountRestricted : meeting.isParticipantAmountRestricted ,
                    participantAmount: parseInt(meeting.participantAmount),
                    isPrivate: meeting.isPrivate ,
                    details: meeting.details,
                    status: meeting.status,
                    tags: meeting.tags
                });
            console.log(this.state);
        })
        PlatformService.getAllPlatforms().then(res => {
            this.setState({platforms: res.data});
            this.setState({selectedMeetingPlatform: res.data[0].name});
        })
        MeetingService.getDTOFieldsList().then(res => {
            this.setState({
                fields: res.data
            })
            this.initFields();
        })
    }
    initFields = () => {
        console.log(this.state.fields)
        this.state.fields?.map( field =>
            this.state.errors[field] = false)
        this.setState({
            errors: this.state.errors
        })
        console.log(this.state.errors)
    }
    saveMeeting = (event) => {
        event.preventDefault();
        let meeting = {
            managerUsername: this.state.currentUser.username,
            managerId: this.state.currentUser.id,
            name: this.state.name,
            about: this.state.about,
            date: this.state.date,
            endDate:this.state.endDate,
            isParticipantAmountRestricted : this.state.isParticipantAmountRestricted,
            participantAmount: this.state.participantAmount,
            isPrivate: this.state.isPrivate,
            details: this.state.details,
            tags: this.state.tags,
            meetingPlatforms: this.state.meetingPlatforms
        };
        console.log(meeting);
        this.initFields();

        MeetingService.createMeeting(meeting).then(res => {
        this.props.history.push('/profile');
        }).catch( error => {
            console.log(JSON.stringify(error))

            error.response.data.nullFields.map(field =>
                this.state.errors[field] = true
            )
            this.setState({
                errorMsg: error.response.data.message,
                errors: this.state.errors
            })
        })


    }

    changeNameHandler = (event) => {
        this.setState({name: event.target.value});
        console.log(event.target.value);
    }

    changeAboutHandler = (event) => {
        this.setState({about: event.target.value});
        console.log(event.target.value);
    }

    changeDateHandler = (event) => {
        this.setState({date: event.target.value});
        console.log(event.target.value);
    }

    changeEndDateHandler = (event) => {
        this.setState({endDate: event.target.value});
        console.log(event.target.value);
    }

    changeParticipantAmountHandler = (event) => {
        this.setState({participantAmount: event.target.value});
        console.log(event.target.value);
    }

    changeIsParticipantAmountRestrictedHandler = (event) => {
        this.setState({isParticipantAmountRestricted: event.target.checked});
        console.log(event.target.checked);
    }

    changePrivateHandler = (event) => {
        this.setState({isPrivate: event.target.checked});
        console.log(event.target.checked);
    }

    changeDetailsHandler = (event) => {
        this.setState({details: event.target.value});
        console.log(event.target.value);
    }

    cancel() {
        this.props.history.push('/users');
    }

    addTag(event) {
        let newValue = document.getElementById('newTagName').value;
        document.getElementById('newTagName').value='';

        if (!newValue ||this.state.tags.includes(newValue))
            return;

        this.setState({tags: this.state.tags.concat(newValue)});
    }

    removeTag(removeValue, event) {
        this.state.tags.splice(this.state.tags.indexOf(removeValue), 1);
        this.setState({tags: this.state.tags});
    }

    onSelectedPlatformChanged(event) {
        this.setState({selectedMeetingPlatform: event.target.value});

        let platform = this.state.meetingPlatforms.filter(platform => platform.name === event.target.value)[0];
        this.setState({selectedMeetingPlatformAddress: platform == null ? "" : platform.address});
    }

    changeSelectedMeetingPlatformAddressHandler = (event) => {
        this.setState({selectedMeetingPlatformAddress: event.target.value});
        console.log(event.target.value);
    }

    addPlatform(event) {
        event.preventDefault();
        if (this.state.selectedMeetingPlatformAddress == null || this.state.selectedMeetingPlatformAddress.length < 1)
            return;
        
        console.log("platform" + this.state.selectedMeetingPlatform)

        let oldMeetingPlatform = this.state.meetingPlatforms.filter(platform => platform.name === this.state.selectedMeetingPlatform);
        if (oldMeetingPlatform.length > 0) {
            oldMeetingPlatform[0].address = this.state.selectedMeetingPlatformAddress;
            this.setState({
                meetingPlatforms: this.state.meetingPlatforms
            });
            this.setState({isOpenNewPlatformOpened: true});
        }
        else {
            PlatformService.getPlatformByName(this.state.selectedMeetingPlatform).then(res => {
                this.state.meetingPlatforms.push({
                    platformId: res.data.id,
                    name: this.state.selectedMeetingPlatform,
                    address: this.state.selectedMeetingPlatformAddress
                });
                this.setState({
                    meetingPlatforms: this.state.meetingPlatforms
                });
                this.setState({isOpenNewPlatformOpened: true});
                console.log(JSON.stringify(this.state.meetingPlatforms))
            })
        }        
    }

    removePlatform(meetingPlatform) {
        console.log("meetingPlatform " + meetingPlatform.name);
        let meetPlatforms = this.state.meetingPlatforms;
        console.log(JSON.stringify(meetPlatforms));

        let val;
        for (val of meetPlatforms) {
            if (val.name === meetingPlatform.name) {
                meetPlatforms.splice(meetPlatforms.indexOf(val), 1);
                this.setState({
                    meetingPlatforms: meetPlatforms
                });
                break;
            }
        }
    }

    openNewPlatform(event) {
        event.preventDefault();
        this.setState({isOpenNewPlatformOpened: false});
    }

    closeNewPlatform(event) {
        event.preventDefault();
        this.setState({isOpenNewPlatformOpened: true});
    }

    render() {
        return (
            <div>   
                <div className="container">
                    <div className="row">
                        <div className="card col-md-6 offset-md-3 offset-md-3">
                            <h3 className="text-center">Create meeting</h3>
                            <div className="card-body">
                                <form>
                                    <div className="form-group row">
                                        <InputLabel htmlFor="name"> Meeting name: </InputLabel>
                                        <TextField required id="name" error={this.state.errors["name"]} label="Required" variant="outlined" fullWidth value={this.state.name} onChange={this.changeNameHandler.bind(this)} />
                                    </div>
                                    <div className="form-group row">
                                        <InputLabel htmlFor="about">About: </InputLabel>
                                        <TextField required id="about" error={this.state.errors["about"]} label="Required" variant="outlined" fullWidth value={this.state.about} onChange={this.changeAboutHandler.bind(this)} />
                                    </div>
                                    <div className="form-group row">
                                        <InputLabel htmlFor="date">Beginning Date: </InputLabel>
                                        <TextField type="datetime-local" required id="date" InputLabelProps={{shrink: true}} error={this.state.errors["date"]} label="Required" variant="outlined" fullWidth value={this.state.date} onChange={this.changeDateHandler.bind(this)} />
                                    </div>
                                    <div className="form-group row">
                                        <InputLabel htmlFor="endDate">End Date: </InputLabel>
                                        <TextField type="datetime-local" id="endDate" InputLabelProps={{shrink: true}} error={this.state.errors["endDate"]} label="Required" variant="outlined" fullWidth value={this.state.endDate} onChange={this.changeEndDateHandler.bind(this)} required/>
                                    </div>
                                    {this.state.isParticipantAmountRestricted && <div className="form-group row">
                                        <InputLabel htmlFor="participant_amount"> ParticipantAmount: </InputLabel>
                                        <TextField type="number" id="participant_amount" InputLabelProps={{shrink: true}} error={this.state.errors["participant_amount"]} label="Required" variant="outlined" fullWidth value={this.state.participantAmount} onChange={this.changeParticipantAmountHandler.bind(this)} required/>
                                    </div>}
                                    <div className="form-group row">
                                        <label htmlFor="is_participant_amount_restricted"> Participant amount restricted? </label>
                                        <input type="checkbox" name="is_participant_amount_restricted" className="form-control" checked={this.state.isParticipantAmountRestricted} onChange={this.changeIsParticipantAmountRestrictedHandler.bind(this)} required/>
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="isPrivate"> Is private?: </label>
                                        <input type="checkbox" name="isPrivate" className="form-control" checked={this.state.isPrivate} onChange={this.changePrivateHandler.bind(this)}/>
                                    </div>
                                    <div className="form-group row">
                                        <InputLabel htmlFor="details">  Details: </InputLabel>
                                        <TextField required id="details" error={this.state.errors["details"]} label="Required" variant="outlined" fullWidth value={this.state.details} onChange={this.changeDetailsHandler.bind(this)} />
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="tags"> Tags </label>                        
                                    </div>
                                    <div className="row" style={{background: "white", borderRadius: "5px", padding: 5, marginBottom: 15, borderColor: "black", border: "2px solid #d5d5d5"}}>
                                    {
                                        this.state.tags.map(
                                            tag => 
                                                <div style={{marginRight: "10px", background: "#ddd", padding: 3, borderRadius: "10px"}}> {tag} <i className="fa fa-times" value={tag} onClick={this.removeTag.bind(this, tag)}></i></div>
                                        )
                                    }
                                    </div>
                                    <div className="row">
                                        <input id="newTagName" type="text" name="addTag" className="form-control col-9"/>
                                        <input type="button" className="btn btn-secondary col-3" onClick={this.addTag.bind(this)} value="Add"/>
                                    </div>
                                    <div className="row">Platforms </div>
                                    {
                                        this.state.meetingPlatforms.map(
                                            platform => 
                                                <div className="row mb-2" style={{ background: "#dadada", borderRadius: "10px" }}>
                                                    <div className="col-2"><img width="20px" height="20px" src="https://computercraft.ru/uploads/monthly_2018_09/discord_logo.0.jpg.7a69ad4c741ee1fb1bd39758714e7da5.jpg"></img></div>
                                                    <div className="col-3">{platform.name}</div>
                                                    <div className="col-6">{platform.address}</div>
                                                    <div className="col-1"><i className="fa fa-times" value={platform} onClick={this.removePlatform.bind(this, platform)}></i></div>
                                                </div>
                                        )
                                    }
                                    
                                    {
                                        this.state.isOpenNewPlatformOpened ? (
                                            <div className="row">
                                                <button className="btn btn-primary col-5 offset-7" onClick={this.openNewPlatform.bind(this)}>Add new platform</button>
                                            </div>
                                        ) : (
                                            <div>
                                            <div className="row mb-2">
                                                <select className="form-control" value={this.state.selectedMeetingPlatform} onChange={this.onSelectedPlatformChanged.bind(this)}>
                                                    {
                                                        this.state.platforms.map(
                                                            platform =>
                                                            <option value={platform.name}>{platform.name} - {platform.info}</option>
                                                        )
                                                    }
                                                </select>
                                            </div>
                                            <div className="mb-2 row">
                                                <label className="col-3" htmlFor="address"> Address: </label>
                                                <input type="text" name="address" className="form-control col-6" value={this.state.selectedMeetingPlatformAddress} onChange={this.changeSelectedMeetingPlatformAddressHandler.bind(this)}/>
                                                <button className="btn btn-success col-3" onClick={this.addPlatform.bind(this)}>Add</button>
                                            </div>
                                            <div className="row mb-4">
                                                <button className="btn btn-danger offset-9 col-3" onClick={this.closeNewPlatform.bind(this)}>Close</button>
                                            </div>
                                        </div>
                                        )
                                    }
                                    
                                    <div className="row">
                                        <button className="btn btn-success" onClick={this.saveMeeting.bind(this)}>Save</button>
                                        <button className="btn btn-danger" onClick={this.cancel.bind(this)} style={{marginLeft: "10px"}}>Cancel</button>
                                    </div>
                                    {this.state.errorMsg && <div className="row">
                                        <label class="text-danger">{this.state.errorMsg}</label>
                                    </div>
                                    }
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}