const uploadApi = () => {
  console.log(window)
  if (Object.hasOwn(window, 'uploadAPI')) {
    return window['uploadAPI']
  }

  // if there is no upload api exposed from electron, then expose a fake api
  return {
    create: () => {}
  }
}

export function createUpload(upload) {
  uploadApi.create(upload)
}