export type EnvironmentVariables = {
	[key: string]: string | undefined
}

export function getEnvironment(): EnvironmentVariables {
	const importMeta = import.meta as object

	if ('env' in importMeta) {
		return importMeta.env as EnvironmentVariables
	}

	if (typeof process !== 'undefined' && process.env) {
		return process.env
	}

	throw new Error('Environment variables are not available')
}
