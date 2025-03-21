const uploadApiDefaults = {
  create: () => {},
  delete: () => {},
  findAll: () => {}
}

const uploadApi = window.uploadAPI ?? uploadApiDefaults

export const createUpload = uploadApi.create
export const deleteUpload = uploadApi.delete
export const findAllUploads = uploadApi.findAll
