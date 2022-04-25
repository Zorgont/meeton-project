import axios from "axios";
import { ACCESS_TOKEN, AM_BASE_URL, API_BASE_URL, GOOGLE, GOOGLE_AUTH_URL } from "../constants/constant";
import authNewHeader from "./AuthNewHeader";
import UserService from "./UserService";

const API_URL = API_BASE_URL + "/api/v1/auth/";

class AuthService {
    getNewCurrentUser() {
        return axios.get(AM_BASE_URL + '/profile', { headers: authNewHeader() });
    }
    updateUsername(username) {
        console.log(AM_BASE_URL + '/updateUsername?username=' + username)
        return axios.get(AM_BASE_URL + '/updateUsername?username=' + username, { headers: authNewHeader() });
    }







    login(username, password) {
        return axios
            .post(API_URL + "signin", {
                username,
                password
            })
            .then(response => {
                console.log(response);
                if (response.data.token) {
                    console.log(JSON.stringify(response.data));
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
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
        return axios.post(API_URL + "signup", {
            username,
            email,
            password
        });
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));

    }
    confirmUser(token) {
        return axios.get(API_URL + "confirmAccount?token=" + token)
    }
}

export default new AuthService();