import axios from "axios";

export const axiosInstance = axios.create();
export const serverBaseUrl = import.meta.env.VITE_SERVER_BASE_URL;
