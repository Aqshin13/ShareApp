import axios from "axios";

const http = axios.create();


let token = JSON.parse(localStorage.getItem("token"));

export function setCurrentToken(data) {
  if (data) {
    token = data;
  }
}

http.interceptors.request.use((config) => {
  if (token) {
    config.headers["Authorization"] = `Bearer ${
      JSON.parse(localStorage.getItem("token")) || token
    }`;
  }
  return config;
});

http.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    const refreshToken = JSON.parse(localStorage.getItem("refresh-token"));
    console.log("1");

    try {
      if (error.response?.status === 401 || error.response?.status === 498) {
        window.location.href = "/login";
        localStorage.removeItem("refresh-token");
        localStorage.removeItem("token");
        localStorage.removeItem("auth");
        return Promise.reject(error);
      }

      if (
        refreshToken &&
        error.response?.status === 499 &&
        !originalRequest._retry
      ) {
        originalRequest._retry = true;
        const newRefreshToken = await axios.post(
          "/api/v1/auth/refreshToken/" + refreshToken
        );
        localStorage.setItem(
          "token",
          JSON.stringify(newRefreshToken.data.token)
        );
        if (!originalRequest.headers) {
          originalRequest.headers = {};
        }
        originalRequest.headers[
          "Authorization"
        ] = `Bearer ${newRefreshToken.data.token}`;

        return axios(originalRequest); // Retry the original request with the new access token.
      }
    } catch (error) {
      localStorage.removeItem("refresh-token");
      localStorage.removeItem("token");
      localStorage.removeItem("auth");
      window.location.href = "/login";
      return Promise.reject(error);
    }

    return Promise.reject(error);
  }
);

export default http;
