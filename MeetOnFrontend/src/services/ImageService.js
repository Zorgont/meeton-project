import axios from 'axios'
import authHeader from "./AuthHeader";
import { API_BASE_URL } from '../constants/constant';

const IMAGE_API_BASE_URL = API_BASE_URL + "/api/v1/users";
class ImageService {

    uploadAvatar(userId, imageFile) {
        return axios.post(IMAGE_API_BASE_URL + '/' + userId + '/avatar', imageFile  , { headers: authHeader() });
    }
}

export default new ImageService()