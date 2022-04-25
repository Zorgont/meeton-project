import React, { Component } from "react";
import { Switch, Route, Link } from "react-router-dom";
import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AuthService from "./services/AuthService";

import NewLoginComponent from "./components/NewLoginComponent";
import Register from "./components/RegisterComponent";
import BoardUser from "./components/BoardUserComponent";
import BoardModerator from "./components/BoardUserComponent";
import BoardAdmin from "./components/BoardUserComponent";
import CreateMeeting from "./components/CreateMeetingComponent";
import MeetingList from "./components/MeetingsComponent";
import MeetingPage from "./components/MeetingPageComponent";
import UpdateMeeting from "./components/ChangeMeetingComponent";
import CreateRequest from "./components/CreateRequestComponent";
import MeetingRequest from "./components/MeetingRequestComponent";
import NotificationBar from "./components/NotificationBarComponent";
import ProfileSettings from "./components/ProfileSettingsComponent";
import ConfirmationCompleted from "./components/ConfirmationCompletedComponent";
import EmailConfirmation from "./components/EmailConfirmationComponent";
import UsernameInputComponent from "./components/UsernameInputComponent";
import UserProfileComponent from "./components/UserProfileComponent";
import UserMenuComponent from "./components/UserMenuComponent";
import OAuth2RedirectHandler from "./services/login/oauth2/OAuth2RedirectHandler";

class App extends Component {
    constructor(props) {
        super(props);
        this.logOut = this.logOut.bind(this);

        this.state = {
            showModeratorBoard: false,
            showAdminBoard: false,
            currentUser: undefined
        };
    }

    componentDidMount() {
        const user = AuthService.getCurrentUser();

        if (user) {
            this.setState({
                currentUser: user,
                // showModeratorBoard: user.roles.includes("ROLE_MODERATOR"),
                // showAdminBoard: user.roles.includes("ROLE_ADMIN")
            });
        }
    }

    logOut() {
        AuthService.logout();
    }

    render() {
        const { currentUser, showModeratorBoard, showAdminBoard } = this.state;

        return (
            <div>
                <nav className="navbar navbar-expand navbar-light" style={{backgroundColor: "white"}}>
                    <Link to={"/"} className="nav-link brand" style={{marginLeft: "30px"}}>
                        meeton
                    </Link>
                    <div className="navbar-nav mr-auto">
                        <li className="nav-item">
                            <Link to={"/meetings"} className="nav-link footer-link">
                                meetings
                            </Link>
                        </li>
                    </div>

                    {currentUser ? (
                        <div className="navbar-nav ml-auto" style={{marginRight: "30px"}}>
                            <li className="nav-item">
                                <Link to="add_meeting" className="without-decoration">
                                    <Button variant="contained" style={{marginTop: "8px", textDecoration: "none"}}><AddIcon/>Create meeting</Button>
                                </Link>
                            </li>
                            <li className="nav-item" style={{marginRight: "18px", marginLeft: "30px"}}>
                                <NotificationBar></NotificationBar>
                            </li>
                            <li className="nav-item">
                                <UserMenuComponent user={currentUser}/>
                            </li>

                        </div>
                    ) : (
                        <div className="navbar-nav ml-auto" style={{marginRight: "30px"}}>
                            <li className="nav-item">
                                <Link to={"/login"} className="nav-link footer-link">
                                    Login
                                </Link>
                            </li>

                            <li className="nav-item">
                                <Link to={"/register"} className="nav-link footer-link">
                                    Sign Up
                                </Link>
                            </li>
                        </div>
                    )}
                </nav>

                <div className="container mt-3">
                    <Switch>
                        <Route exact path={["/", "/home"]} component={MeetingList} />
                        <Route exact path="/login" component={NewLoginComponent} />
                        <Route exact path="/register" component={Register} />
                        <Route exact path="/users/:username" component={UserProfileComponent}  />
                        <Route exact path="/profile" component={UserProfileComponent} />
                        <Route exact path="/profile/update" component={ProfileSettings} />
                        <Route exact path="/user" component={BoardUser} />
                        <Route exact path="/mod" component={BoardModerator} />
                        <Route exact path="/admin" component={BoardAdmin} />
                        <Route exact path="/add_meeting" component={CreateMeeting} />
                        <Route exact path="/meetings" component={MeetingList} />
                        <Route exact path="/confirm" component={ConfirmationCompleted} />
                        <Route exact path="/confirm_email" component={EmailConfirmation} />
                        <Route exact path="/meetings/:id" component={MeetingPage} />
                        <Route path="/meetings/:id/requests" component={MeetingRequest} />
                        <Route path="/update/:id" component={UpdateMeeting} />
                        <Route path="/enroll/:id" component={CreateRequest} />
                        <Route exact path="/usernameInput" component={UsernameInputComponent} />
                        <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}></Route>
                    </Switch>
                </div>
            </div>
        );
    }
}

export default App;