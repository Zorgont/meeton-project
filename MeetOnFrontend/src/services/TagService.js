import axios from 'axios'
import { API_BASE_URL } from '../constants/constant';

const TAGS_API_BASE_URL = API_BASE_URL + "/api/v1/tags";
class TagService {
    getTags() {
        return axios.get(TAGS_API_BASE_URL);
    }
}

export default new TagService();