export type AppEnv = 'dev' | 'prod' | 'preview'

export function appEnv(): AppEnv {
	if (import.meta.env.MODE === 'development') {
		return 'dev'
	}

	return 'prod'
}
