import axios from 'axios'
import { API_BASE_URL } from '../constants/constant';
import authHeader from "./AuthHeader";

const REQUEST_API_BASE_URL = API_BASE_URL + "/api/v1/comments";

class CommentService{
    createComment(comment) {
        return axios.post(REQUEST_API_BASE_URL, comment, { headers: authHeader() });
    }

    getCommentsByUserId(userId) {
        return axios.get(REQUEST_API_BASE_URL + '/byUser/' + userId, { headers: authHeader() });
    }

    getCommentsByMeetingId(meetingId) {
        console.log(REQUEST_API_BASE_URL + '/byMeeting/' + meetingId)
        return axios.get(REQUEST_API_BASE_URL + '/byMeeting/' + meetingId, { headers: authHeader() });

    }

    removeComment(id) {
        return axios.delete(REQUEST_API_BASE_URL + '/'+ id, { headers: authHeader() });
    }
}
export default new CommentService()