import axios from 'axios'
import authHeader from "./AuthHeader";
import { API_BASE_URL } from '../constants/constant';

const SCORE_API_BASE_URL = API_BASE_URL + "/api/v1/score";
class ScoreService {
    getAggregatedScore(meetingId) {
        return axios.get(SCORE_API_BASE_URL + '/' + meetingId + '/aggregated', { headers: authHeader() });
    }
    setScore(meetingId, score) {
        return axios.post(SCORE_API_BASE_URL + '/' + meetingId, score, { headers: authHeader() });
    }
    getUserScore(meetingId, userId) {
        return axios.get(SCORE_API_BASE_URL + '/' + meetingId + '/byUser/' + userId, { headers: authHeader() });
    }
}

export default new ScoreService()