package internal

import (
	"github.com/google/uuid"
	"time"
)

type FileUpload struct {
	Id       uint      `gorm:"column:_id;primaryKey"`
	Uuid     uuid.UUID `gorm:"column:uuid"`
	Name     string    `gorm:"column:name"`
	MimeType string    `gorm:"column:mime_type"`
	Size     uint      `gorm:"column:size"`
	// UploadedAt is the uploaded date in unix epoch milliseconds
	UploadedAt time.Time `gorm:"column:uploaded_at"`
}
