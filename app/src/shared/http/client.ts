import axios from 'axios'
import { getEnvironment } from '../environment.ts'

const env = getEnvironment()

export const axiosInstance = axios.create()
export const serverBaseUrl = env.VITE_SERVER_BASE_URL
