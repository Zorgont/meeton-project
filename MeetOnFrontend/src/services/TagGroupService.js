import axios from 'axios'
import authHeader from "./AuthHeader";
import { API_BASE_URL } from '../constants/constant';

const TAGS_API_BASE_URL = API_BASE_URL + "/api/v1/users";
class TagGroupService {
    getTagGroups(userId) {
        return axios.get(TAGS_API_BASE_URL + '/' + userId + '/tagGroups' , { headers: authHeader() });
    }
    createTagGroup(userId, tagGroup){
        return axios.post(TAGS_API_BASE_URL + '/' + userId + '/tagGroups' , tagGroup , { headers: authHeader() });
    }
    setNotifiable(userId, tagGroupId, isNotifiable){
        return axios.put(TAGS_API_BASE_URL + '/' + userId + '/tagGroups/' + tagGroupId + '?isNotifiable=' + isNotifiable , null ,{ headers: authHeader() })
    }
    deleteTagGroup(userId,tagGroupId){
        return axios.delete(TAGS_API_BASE_URL + '/' + userId + '/tagGroups/' + tagGroupId ,{ headers: authHeader() } )
    }
}

export default new TagGroupService();