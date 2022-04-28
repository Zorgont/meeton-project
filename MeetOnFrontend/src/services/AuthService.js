import axios from "axios";
import { ACCESS_TOKEN, AM_BASE_URL, API_BASE_URL, GOOGLE, GOOGLE_AUTH_URL } from "../constants/constant";
import authHeader from "./AuthHeader";
import UserService from "./UserService";

const API_URL = API_BASE_URL + "/meeton-core/v1/auth/";
const AM_URL = AM_BASE_URL + "/authentication-manager/v1/auth";

class AuthService {
    getNewCurrentUser() {
        return axios.get(AM_URL + '/profile', { headers: authHeader() });
    }
    updateUsername(username) {
        console.log(AM_URL + '/updateUsername?username=' + username)
        return axios.get(AM_URL + '/updateUsername?username=' + username, { headers: authHeader() });
    }

    login(username, password) {
        return axios
            .post(AM_URL + "/signin", {
                username,
                password
            })
            .then(res => {
                console.log(JSON.stringify(res.data));
                localStorage.setItem("user", JSON.stringify(res.data.user));
                localStorage.setItem(ACCESS_TOKEN, res.data.token);
                return res.data;
            });
    }

    loginViaGoogle(user) {
        return axios
            .post(API_URL + "google", user)
            .then(response => {
                console.log(response);
                if (response.data.token) {
                    console.log(JSON.stringify(response.data));
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    existsUserByUsername(username) {
        return axios.get(API_URL + 'existsByName/' + username);
    }

    existsUserByEmail(email) {
        return axios.get(API_URL + 'existsByEmail/' + email);
    }

    logout() {
        localStorage.removeItem("user");
        localStorage.removeItem("username");
        localStorage.removeItem(ACCESS_TOKEN);
    }

    register(username, email, password) {
        return axios.post(AM_URL + "/signup", {
            username,
            email,
            password
        });
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));

    }
    confirmUser(token) {
        console.log("herhrerer")
        return axios.get(AM_URL + "/confirmAccount?token=" + token)
    }
}

export default new AuthService();