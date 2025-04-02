import axios from "axios";

export const axiosInstance = axios.create();

// TODO: guardar en una variable la url base del servidor
export const serverBaseUrl = 'http://localhost:8080';