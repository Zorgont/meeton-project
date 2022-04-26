import axios from 'axios'
import authHeader from "./AuthHeader";
import { API_BASE_URL } from '../constants/constant';

const REQUEST_API_BASE_URL = API_BASE_URL + "/meeton-core/v1/requests";

class RequestService{
    createRequest(request) {
        return axios.post(REQUEST_API_BASE_URL, request, { headers: authHeader() });
    }

    getRequestsByUserId(userId) {
        return axios.get(REQUEST_API_BASE_URL + '/byUser/' + userId, { headers: authHeader() });
    }

    getRequestsByMeetingId(meetingId) {
        return axios.get(REQUEST_API_BASE_URL + '/byMeeting/' + meetingId, { headers: authHeader() });
    }

    updateRequestStatus(requestId, status) {
        return axios.put(REQUEST_API_BASE_URL + `/changeStatus/${requestId}?status=${status}`, null, { headers: authHeader() });
    }

    getRequestByMeetingAndUser(meetingId, userId) {
        return axios.get(REQUEST_API_BASE_URL + `/by?meetingId=${meetingId}&userId=${userId}`, { headers: authHeader() });
    }

    removeRequest(id) {
        return axios.delete(REQUEST_API_BASE_URL + '/'+ id, { headers: authHeader() });
    }
    getAprovedRequestsAmount(meetingId){
        return axios.get(REQUEST_API_BASE_URL + '/amount/' + meetingId, { headers: authHeader() });
    }
    getPendingRequests(meetingId){
        return axios.get(REQUEST_API_BASE_URL + '/pendingRequests/' + meetingId, { headers: authHeader() });
    }
}
export default new RequestService()