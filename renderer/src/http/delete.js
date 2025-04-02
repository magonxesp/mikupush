import { axiosInstance } from './client.js'

export async function deleteUpload(id) {
  const response = await axiosInstance.delete(`http://localhost:8080/api/file/${id}`, {
    headers: {
      "Accept": "application/json",
      "Content-Type": "application/json",
    },
  })

  if (response.status !== 200) {
    throw new Error(`file delete request failed with status ${response.status}`)
  }
}
