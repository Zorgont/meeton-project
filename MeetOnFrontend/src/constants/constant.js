export const API_GATEWAY_URL = "http://localhost:8989";
export const API_BASE_URL = API_GATEWAY_URL;
export const WS_BASE_URL = API_GATEWAY_URL;
export const AM_BASE_URL = API_GATEWAY_URL;

export const ACCESS_TOKEN = "accessToken";

export const OAUTH2_REDIRECT_URI = "http://localhost:3000/oauth2/redirect";

export const GOOGLE = "google";
export const GOOGLE_AUTH_URL = AM_BASE_URL + "/oauth2/authorize/google?redirect_uri=" + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = AM_BASE_URL + "/oauth2/authorize/github?redirect_uri=" + OAUTH2_REDIRECT_URI;