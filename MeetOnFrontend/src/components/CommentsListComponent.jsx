import React, {Component} from "react";
import Avatar from '@material-ui/core/Avatar';
import { Link } from 'react-router-dom';
import AuthService from "../services/AuthService";
import { API_BASE_URL } from '../constants/constant';

export default class CommentsList extends Component{
    constructor(props) {
        super(props);
        this.state={
            currentUser: AuthService.getCurrentUser(),
            content:""
        }
    }
    SaveComment=(content)=>{
        this.props.onCommentChange(content);
        this.setState({
            content:""
        })
    }
    ChangeContent=(event)=>{
        this.setState({
            content:event.target.value
        })
    }
    render(){
        return (
            <div className="container">
                {this.state.currentUser &&
                <div className="row">
                    <div className="col">
                        <label htmlFor="add_comment"> Add comment </label>
                        <input type="text"  name="add_comment" className="form-control" value={this.state.content} onChange={this.ChangeContent.bind(this)} />
                    </div>
                </div>
                }
                {this.state.currentUser &&
                <div className="row mt-1">
                    <div className="col">
                        <button className="btn btn-success" onClick={this.SaveComment.bind(this,this.state.content)}>Post</button>
                    </div>
                </div>
                }
                {!this.props.comments.isEmpty?this.props.comments.map(
                comment =>
                    <div className="container mt-4" style={{border: "1px solid #ddd", borderRadius: "10px"}}>
                    <Link to={`/users/${comment.username}`} style={{textDecoration: "none", color: "black"}}>
                        <div className="row mt-2">
                            <div className="col-2"><Avatar src={API_BASE_URL + `/meeton-core/v1/users/${comment.username}/avatar`}/></div>
                            <div className="col-3 mt-2" style={{marginLeft: "5px"}}>
                                <div>
                                    <p>{comment.username}</p>
                                </div>
                            </div>
                            <div className="col mt-2">
                                {comment.date.slice(0, -3)}
                            </div>
                        </div>
                    </Link>
                    <div className="row">
                        <div className="col offset-1">
                            <p style={{wordBreak: "break-word"}}>{comment.content}</p>
                        </div>
                    </div>
                    </div>
                ):<div />
                }
            </div>
        )
    }
}


