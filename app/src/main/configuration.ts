import { appEnv } from '../shared/helpers/environment.ts'

export const serverBaseUrl = () => {
	if (appEnv() === 'dev') {
		return 'http://localhost:8080'
	} else {
		return 'https://mikupush.io'
	}
}
