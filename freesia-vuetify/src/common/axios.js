import axios from "axios";

// Axios 인스턴스 생성
export const $axios = axios.create({
  baseURL: "http://localhost:8080",
});
