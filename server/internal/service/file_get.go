package service

import (
	"fmt"
	"mikupush.io/internal"
	"os"
	"path"
)

type FileUploadContent struct {
	Content  []byte
	Name     string
	Size     uint
	MimeType string
}

func GetFileContents(uuid string) (*FileUploadContent, error) {
	db := internal.GetDatabase()

	var fileUpload *internal.FileUpload
	result := db.Where("uuid = ?", uuid).First(&fileUpload)
	if result.Error != nil {
		return nil, result.Error
	}

	if fileUpload == nil {
		return nil, nil
	}

	filePath := path.Join(internal.GetDataDir(), uuid)
	content, err := os.ReadFile(filePath)
	if err != nil {
		return nil, fmt.Errorf("error reading content of file with id %s: %w", uuid, err)
	}

	fileContent := &FileUploadContent{
		Name:     fileUpload.Name,
		Size:     fileUpload.Size,
		MimeType: fileUpload.MimeType,
		Content:  content,
	}

	return fileContent, nil
}
