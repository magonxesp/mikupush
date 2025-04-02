package service

import (
	"errors"
	"fmt"
	"io"
	"os"
	"path"
	"time"

	"github.com/google/uuid"
	"gorm.io/gorm"
	"mikupush.io/internal"
)

type FileCreateRequest struct {
	Uuid     uuid.UUID `json:"uuid"`
	Name     string    `json:"name"`
	MimeType string    `json:"mime_type"`
	Size     uint      `json:"size"`
}

var ErrFileExists = errors.New("file exists")

func CreateFile(request *FileCreateRequest) error {
	db := internal.GetDatabase()

	var fileUpload internal.FileUpload
	result := db.Where("uuid = ?", request.Uuid).First(&fileUpload)
	if result.Error != nil && !errors.Is(result.Error, gorm.ErrRecordNotFound) {
		return result.Error
	}

	if result.RowsAffected > 0 {
		return ErrFileExists
	}

	fileUpload = internal.FileUpload{
		Uuid:       request.Uuid,
		Name:       request.Name,
		MimeType:   request.MimeType,
		Size:       request.Size,
		UploadedAt: time.Now(),
	}

	result = db.Create(&fileUpload)
	return result.Error
}

func SaveFileContents(uuid string, reader io.ReadCloser) error {
	db := internal.GetDatabase()

	var fileUpload internal.FileUpload
	result := db.Where("uuid = ?", uuid).First(&fileUpload)
	if result.Error != nil {
		return result.Error
	}

	file, err := os.Create(path.Join(internal.GetDataDir(), uuid))
	if err != nil {
		return fmt.Errorf("failed creating data directory: %w", err)
	}

	defer file.Close()

	_, err = io.Copy(file, reader)
	if err != nil {
		return fmt.Errorf("failed writting file: %w", err)
	}

	return nil
}
