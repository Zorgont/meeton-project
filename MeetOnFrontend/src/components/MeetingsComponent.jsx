import React, { Component } from "react";
import { Link } from 'react-router-dom';
import MeetingService from '../services/MeetingService';
import TagService from '../services/TagService';
import AuthService from "../services/AuthService";
import Switch from '@material-ui/core/Switch';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import {Pagination} from "@material-ui/lab";
import MeetingGroupComponent from "./MeetingGroupComponent";
import {List, ListItem, ListItemText} from "@material-ui/core";
import MeetingCardComponent from "./MeetingCardComponent";

class MeetingList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            meetings: [],
            tags: [],
            selectedTags: [],
            currentUser: AuthService.getCurrentUser(),
            page: 1,
            pagesNumber: 5,
            managerAvatar: [],
            favoriteMeetings: [],
            trendMeetings: []
        }
    }

    componentDidMount() {
        let script = document.createElement("script");
        script.src = "https://code.jquery.com/jquery-3.1.0.js";
        script.async = true;
        document.body.appendChild(script);

        script = document.createElement("script");
        script.src = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js";
        script.async = true;
        document.body.appendChild(script);

        this.getRecommendations()
        this.getListByTags();

        TagService.getTags().then((res) => {
            this.setState({tags: res.data});
        });
    }

    onTagChecked(tag, event) {
        let isChecked = event.target.checked;
        console.log("checked? " + isChecked);

        let array = this.state.selectedTags;
        if (isChecked && !array.includes(tag))
            array.push(tag);
        else if (!isChecked && array.includes(tag))
            array.splice(array.indexOf(tag), 1);

        this.getListByTags();

        this.getRecommendations()
        this.setState({selectedTags: array});
    }

    getListByTags() {
        MeetingService.getPagesNumberByTags(this.state.selectedTags).then((res) => {
            this.setState({
                pagesNumber: res.data
            })

            MeetingService.getMeetingsByTags(this.state.selectedTags, this.state.page).then((res) => {
                this.setState({meetings: res.data});
                console.log(this.state.meetings);
            });
        })
    }

    getRecommendations(){
        if(this.state.currentUser)
            MeetingService.getRecommendedMeetingsByTags(null, this.state.page).then((res) => {
                console.log(res.data)
                let favouriteCarousel = []
                for(var index = 0; index < res.data[0].length; index++) {
                    for (let i in res.data) {
                        let list = res.data[i]
                        if (list[index] === null || list[index] === undefined)
                            continue
                        if(favouriteCarousel.filter(meeting => meeting.meetingId === list[index].meetingId).length === 0) {
                            favouriteCarousel.push(list[index])
                        }
                        if(favouriteCarousel.length === 10) {
                            index = Number.MAX_VALUE;
                            break;
                        }
                    }
                }

                // console.log(res.data)
                // let favCarousel = []
                // let ind = 0
                // while (favCarousel.length < 10) {
                //     let errorCount = 0
                //     for(let i in res.data) {
                //         try {
                //             if(favCarousel.filter(meeting => meeting.meetingId === res.data[i][ind].meetingId).length === 0) {
                //                 favCarousel.push(res.data[i][ind])
                //             }
                //         }
                //         catch {
                //             errorCount++
                //             continue
                //         }
                //     }
                //     if (errorCount === res.data.length)
                //         break
                //
                //     ind++
                // }
                console.log(favouriteCarousel)
                this.setState({favoriteMeetings: favouriteCarousel});
                console.log(this.state.favoriteMeetings);
            });
    }

    changePage(event, newPage){
        this.state.page = newPage;
        this.getListByTags()
        this.setState({
            page: this.state.page
        })
    }

    render() {
        return (
            <div className="container">
                {(this.state.currentUser && this.state.favoriteMeetings?.length > 0) && <div>
                    <div className="row">
                        <div className="col">
                            <h2 className="text-center">Favorite meetings</h2>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col"/>
                        <div className="col">
                            <MeetingGroupComponent meetings={this.state.favoriteMeetings}/>
                        </div>
                        <div className="col"/>
                    </div>
                </div>}
                <div className="row mt-4">
                    <div className="col-2">
                        <List className="shadow-container">
                            {['Trends', 'Recommendations', 'All'].map((text, index) => (
                                <ListItem button key={text}>
                                    <ListItemText primary={text} />
                                </ListItem>
                            ))}
                        </List>
                    </div>
                    <div className="col">
                        <div className="row">
                        {
                            this.state.meetings?.map((meetingEntity) =>
                                <div className="col-6 mb-2">
                                    <MeetingCardComponent meeting={meetingEntity} />
                                </div>
                        )}
                        </div>
                        <div className="row justify-content-center" >
                            <Pagination count={this.state.pagesNumber} color="primary" hideNextButton={this.state.hideNextButton} onChange={this.changePage.bind(this)} />
                        </div>
                    </div>
                    <div className="col-2">
                        <div className="shadow-container" style={{paddingLeft: "10px", paddingRight: "20px"}}>
                            <h2 className="text-center">Filtration</h2>
                            <FormGroup>
                                {
                                    this.state.tags.map(tag =>
                                        <FormControlLabel
                                            control={<Switch id={tag} size="small"  onChange={this.onTagChecked.bind(this, tag)} />}
                                            label={tag}
                                        />
                                    )
                                }
                            </FormGroup>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default MeetingList;