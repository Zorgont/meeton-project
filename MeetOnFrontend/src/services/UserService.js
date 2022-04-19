import axios from 'axios'
import { API_BASE_URL } from '../constants/constant';
import authHeader from "./AuthHeader";

const USER_API_BASE_URL = API_BASE_URL + "/api/v1/users";
class UserService {
    getUsers() {
        return axios.get(USER_API_BASE_URL,{ headers: authHeader() });
    }

    createUser(user) {
        return axios.post(USER_API_BASE_URL, user , { headers: authHeader() });
    }

    getUserById(userId) {
        return axios.get(USER_API_BASE_URL + '/' + userId , { headers: authHeader() });
    }

    updateUser(user, userId) {
        return axios.put(USER_API_BASE_URL + '/' + userId, user , { headers: authHeader() });
    }

    deleteUser(userId) {
        return axios.delete(USER_API_BASE_URL + '/' + userId , { headers: authHeader() });
    }

    updateUserSettings(user, userId) {
        return axios.put(USER_API_BASE_URL + '/changeSettings/' + userId , user , { headers: authHeader()})
    }
}

export default new UserService()