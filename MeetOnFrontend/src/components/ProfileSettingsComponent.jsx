// eslint-disable-next-line no-unused-vars
import React, {Component} from "react";
import AuthService from "../services/AuthService";
import UserService from "../services/UserService";
import TagGroupService from "../services/TagGroupService";
import Avatar from "@material-ui/core/Avatar";
import EditOutlinedIcon from "@material-ui/icons/EditOutlined";
import ImageService from "../services/ImageService";
import { API_BASE_URL } from '../constants/constant';

export default class ProfileSettingsComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            firstName: "",
            secondName: "",
            about: "",
            tagGroups: null,
            tags: [],
            isNotifiable: false,
            tagsNotifiable: [],
            errorMsg: null,
            selectedFile: null
        };
    }
    updateUser = (event) => {
        event.preventDefault()
        let user = {
            firstName: this.state.firstName,
            secondName: this.state.secondName,
            about: this.state.about
        }
        UserService.updateUserSettings(user, this.state.currentUser.id).then(res => {
            this.props.history.push("/profile")
        }).catch( error => {
            this.setState({
                errorMsg: error.response.data.message
            })
        });
    }
    changeFirstNameHandler = (event) => {
        this.setState({firstName: event.target.value});
    }

    changeSecondNameHandler = (event) => {
        this.setState({secondName: event.target.value});
    }

    changeAboutHandler = (event) => {
        this.setState({about: event.target.value});
    }
    changeIsNotifiableHandler = (event) => {
        this.setState({isNotifiable: event.target.checked});
    }
    changeTagGroupIsNotifiableHandler = (index,event) => {
        TagGroupService.setNotifiable(this.state.currentUser.id, this.state.tagGroups[index].groupId, event.target.checked);
        this.state.tagGroups[index].isNotifiable = event.target.checked
        this.setState({
            tagGroups: this.state.tagGroups});
        this.setState({
            tagsNotifiable: this.state.tagGroups.map(tag => tag.isNotifiable)
        })
    }
    changeAvatarFile = (event) => {
        console.log(event.target);
        let file = event.target.files[0];
        this.setState({
            selectedFile: file
        })
    }
    uploadAvatar(e) {
        e.preventDefault();
        console.log(this.state.selectedFile)
        const uploadImageData = new FormData();
        uploadImageData.append('imageFile', this.state.selectedFile, this.state.selectedFile.name);
        console.log(uploadImageData);
        ImageService.uploadAvatar(this.state.currentUser.id, uploadImageData).then((res) => {
            this.setState({currentUser: this.state.currentUser})
            window.location.reload();
        })
    }

    cancel() {
        this.props.history.push('/profile');
    }
    addTag(event) {
        let newValue = document.getElementById('newTagName').value;
        document.getElementById('newTagName').value='';

        if (!newValue || this.state.tags.includes(newValue))
            return;

        this.setState({tags: this.state.tags.concat(newValue)});
    }
    removeTag(removeValue, event) {
        this.state.tags.splice(this.state.tags.indexOf(removeValue), 1);
        this.setState({tags: this.state.tags});
    }
    removeTagGroup(removeValue, event) {
        event.preventDefault()
        console.log(removeValue)
        this.state.tagGroups.splice(this.state.tagGroups.indexOf(removeValue), 1);
        this.setState({tagGroups: this.state.tagGroups});
        TagGroupService.deleteTagGroup(this.state.currentUser.id, removeValue.groupId)
    }
    addTagGroup(addValue, isNotifiable, event){
        let tagGroup={
            tags: addValue,
            userId: this.state.currentUser.id,
            isNotifiable: isNotifiable
        }
        console.log(tagGroup)
        this.state.tagGroups.push(tagGroup)
        this.state.tagsNotifiable.push(isNotifiable)
        this.setState({
            tagGroups: this.state.tagGroups,
            tagsNotifiable: this.state.tagsNotifiable,
            tags: [],
            isNotifiable: false,
        })
        TagGroupService.createTagGroup(this.state.currentUser.id, tagGroup).catch( error => {
            this.setState({
                errorMsg: error.response.data.message
            })
        });
    }
    componentDidMount() {
        UserService.getUserById(this.state.currentUser.id).then( res => {
            let user = res.data;
                this.setState({
                    firstName: user.firstName,
                    secondName: user.secondName,
                    about: user.about
                })
        })
        TagGroupService.getTagGroups(this.state.currentUser.id).then( res => {
            this.setState({
                tagGroups: res.data,
            })
            this.setState({
                tagsNotifiable: this.state.tagGroups.map(tag => tag.isNotifiable)
            })

        })
    }
    render() {
        return (
            <div>
                <div className="container">
                    <div className="row">
                        <div className="card col-md-6 offset-md-3 offset-md-3">
                            <h3 className="text-center">Profile settings</h3>
                            <div className="row">
                                <div className="col d-flex justify-content-center">
                                    <div style={{position: "relative"}}>
                                        <Avatar style={{width: "130px", height: "130px"}} src={API_BASE_URL + `/api/v1/users/${this.state.currentUser?.id}/avatar`}/>
                                    </div>
                                </div>
                            <h4 className="text-center">{this.state.currentUser.username}</h4>
                            <div className="card-body">
                                <form>
                                    <div className = "row">
                                        <label htmlFor="changeAvatar">Change avatar: </label>
                                    </div>
                                    <div className = "row">
                                        <input name="changeAvatar" type="file" onChange={this.changeAvatarFile.bind(this)}/>
                                    </div>
                                    <div className = "row">
                                        <button className="btn btn-success" onClick={this.uploadAvatar.bind(this)}>Save</button>
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="firstName"> Firstname: </label>
                                        <input type="text" name="firstName" className="form-control" value={this.state.firstName} onChange={this.changeFirstNameHandler.bind(this)}  />
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="secondName"> Lastname: </label>
                                        <input type="text" name="secondName" className="form-control" value={this.state.secondName} onChange={this.changeSecondNameHandler.bind(this)} />
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="about"> About: </label>
                                        <input type="text"  name="about" className="form-control" value={this.state.about} onChange={this.changeAboutHandler.bind(this)}  />
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="tags"> Preferred tags: </label>
                                    </div>

                                        {
                                            this.state.tagGroups?.map(
                                                (tagGroup,index) =>
                                                     <div className="row" style={{background: "white", borderRadius: "5px", padding: 5, marginBottom: 15, borderColor: "black", border: "2px solid #d5d5d5"}}>
                                                        {
                                                            tagGroup?.tags.map(
                                                                tag =>
                                                                    <div style={{marginRight: "10px", background: "#ddd", padding: 3, borderRadius: "10px"}}> {tag} </div>
                                                            )
                                                        }
                                                         <input type="checkbox" key={index} className="form-control-sm" checked={this.state.tagsNotifiable[index]} onChange={this.changeTagGroupIsNotifiableHandler.bind(this, index)} />
                                                         <i className="fa fa-times" value={tagGroup.id} onClick={this.removeTagGroup.bind(this, tagGroup)}/>
                                                </div>

                                            )
                                        }
                                    <div className="form-group row">
                                        <label htmlFor="tags"> Add new group: </label>
                                    </div>
                                    {this.state.tags.length > 0 && <div className="row" style={{background: "white", borderRadius: "5px", padding: 5, marginBottom: 15, borderColor: "black", border: "2px solid #d5d5d5"}}>
                                        {
                                            this.state.tags.map(
                                                tag =>
                                                    <div style={{marginRight: "10px", background: "#ddd", padding: 3, borderRadius: "10px"}}> {tag} <i className="fa fa-times" value={tag} onClick={this.removeTag.bind(this, tag)}></i></div>
                                            )
                                        }
                                    </div>
                                        }
                                    <div className="row">
                                        <input id="newTagName" type="text" name="addTag" className="form-control col-9"/>
                                        <input type="button" className="btn btn-secondary col-3" onClick={this.addTag.bind(this)} value="Add"/>
                                    </div>
                                    <div className="form-group row">
                                        <label htmlFor="isNotifiable"> Is group Notifiable? </label>
                                        <input type="checkbox" name="isNotifiable" className="form-control-sm" checked={this.state.isNotifiable} onChange={this.changeIsNotifiableHandler.bind(this)}/>
                                    </div>
                                    <div className="row">
                                        <button className="btn btn-success" onClick={this.addTagGroup.bind(this,this.state.tags,this.state.isNotifiable)}>Save tag group</button>
                                    </div>
                                    <div className="row">
                                        <button className="btn btn-success" onClick={this.updateUser.bind(this)}>Save</button>
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
            </div>
        );
    }
}