import axios from 'axios'
import { API_BASE_URL } from '../constants/constant';
import authHeader from "./AuthHeader";

const COMMENT_API_BASE_URL = API_BASE_URL + "/meeton-core/v1/comments";

class CommentService{
    createComment(comment) {
        return axios.post(COMMENT_API_BASE_URL, comment, { headers: authHeader() });
    }

    getCommentsByUserId(userId) {
        return axios.get(COMMENT_API_BASE_URL + '/byUser/' + userId, { headers: authHeader() });
    }

    getCommentsByMeetingId(meetingId) {
        console.log(COMMENT_API_BASE_URL + '/byMeeting/' + meetingId)
        return axios.get(COMMENT_API_BASE_URL + '/byMeeting/' + meetingId, { headers: authHeader() });

    }

    removeComment(id) {
        return axios.delete(COMMENT_API_BASE_URL + '/'+ id, { headers: authHeader() });
    }
}
export default new CommentService()