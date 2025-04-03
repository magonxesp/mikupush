package utils

import "mikupush.io/internal"

func ExceedsUploadSizeLimit(size uint) bool {
	return internal.IsUploadSizeLimited() && size > internal.GetUploadLimit()
}
