const uploadApiDefaults = {
  create: () => {},
  delete: () => {},
  findAll: () => {}
}

function uploadApi() {
  if (Object.hasOwn(window, 'uploadAPI')) {
    return window['uploadAPI']
  }

  // if there is no upload api exposed from electron, then expose a fake api
  return uploadApiDefaults
}

export function createUpload(upload) {
  uploadApi().create(upload)
}

export function deleteUpload(uploadId) {
  uploadApi().delete(uploadId)
}

export async function findAllUploads() {
  return await uploadApi().findAll()
}
