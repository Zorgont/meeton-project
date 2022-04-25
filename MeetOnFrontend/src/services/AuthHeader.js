import { ACCESS_TOKEN } from "../constants/constant";

export default function authHeader() {
    if (localStorage.getItem(ACCESS_TOKEN)) {
        return { Authorization: 'Bearer ' + localStorage.getItem(ACCESS_TOKEN) };
    };

    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.token) {
        return { Authorization: 'Bearer ' + user.token };
    } else {
        return {};
    }
}