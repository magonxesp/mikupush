package service

import (
	"os"
	"path"

	"mikupush.io/internal"
)

func DeleteFile(uuid string) error {
	db := internal.GetDatabase()

	var fileUpload internal.FileUpload
	result := db.Where("uuid = ?", uuid).First(&fileUpload)
	if result.Error != nil {
		return result.Error
	}

	result = db.Delete(&fileUpload)
	if result.Error != nil {
		return result.Error
	}

	filePath := path.Join(internal.GetDataDir(), uuid)
	return os.Remove(filePath)
}
