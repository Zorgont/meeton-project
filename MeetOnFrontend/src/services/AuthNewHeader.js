import { ACCESS_TOKEN } from "../constants/constant";

export default function authNewHeader() {
    return { Authorization: 'Bearer ' + localStorage.getItem(ACCESS_TOKEN) };
}