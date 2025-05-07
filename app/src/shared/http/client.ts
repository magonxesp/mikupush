import axios from 'axios'
import { appEnv } from '../helpers/environment.ts'

export const axiosInstance = axios.create()
export const serverBaseUrl = () => {
	if (appEnv() === 'dev') {
		return 'http://localhost:8080'
	} else {
		return 'https://mikupush.io'
	}
}
