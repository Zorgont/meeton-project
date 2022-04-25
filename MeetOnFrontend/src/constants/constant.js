export const API_BASE_URL = "http://localhost:8080";
export const AM_BASE_URL = "http://localhost:8081";

export const ACCESS_TOKEN = "accessToken";

export const OAUTH2_REDIRECT_URI = "http://localhost:3000/oauth2/redirect";

export const GOOGLE = "google";
export const GOOGLE_AUTH_URL = AM_BASE_URL + "/oauth2/authorize/google?redirect_uri=" + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = AM_BASE_URL + "/oauth2/authorize/github?redirect_uri=" + OAUTH2_REDIRECT_URI;