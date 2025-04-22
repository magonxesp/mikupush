import { axiosInstance, serverBaseUrl } from './client'

export async function deleteUpload(id: string) {
	const response = await axiosInstance.delete(`${serverBaseUrl}/api/file/${id}`, {
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
		},
	})

	if (response.status !== 200) {
		throw new Error(`file delete request failed with status ${response.status}`)
	}
}
