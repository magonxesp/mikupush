export type ClassProperties<T> = {
	// eslint-disable-next-line @typescript-eslint/no-unsafe-function-type
	[K in keyof T as T[K] extends Function ? never : K]:
	T[K] extends object
		? ClassProperties<T[K]>
		: T[K]
};
