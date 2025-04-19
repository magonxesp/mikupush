package utils

import "mikupush.io/server/internal"

func ExceedsUploadSizeLimit(size uint) bool {
	return internal.IsUploadSizeLimited() && size > internal.GetUploadLimit()
}
