package main

import (
	"fmt"
	"net/http"
	"os"
	"path"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

type UploadedFile struct {
	Id       uint      `gorm:"column:_id;primaryKey"`
	Uuid     uuid.UUID `gorm:"column:uuid"`
	Name     string    `gorm:"column:name"`
	MimeType string    `gorm:"column:mime_type"`
	Size     uint      `gorm:"column:size"`
	// UploadedAt is the uploaded date in unix epoch milliseconds
	UploadedAt time.Time `gorm:"column:uploaded_at"`
}

func FileHandler(c *gin.Context) {
	db := GetDatabase()

	fileId := c.Param("uuid")
	fileUuid, err := uuid.Parse(fileId)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": fmt.Sprintf("Failed to parse file id: %s", err.Error()),
		})
		return
	}

	var uploadedFile UploadedFile
	result := db.Where("uuid = ?", fileUuid).First(&uploadedFile)
	if result.Error != nil {
		c.Status(http.StatusNotFound)
		return
	}

	filePath := path.Join(GetDataDir(), fileId)
	content, err := os.ReadFile(filePath)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": fmt.Sprintf("Failed to read file: %s", err.Error()),
		})
		return
	}

	c.Header("Content-Type", uploadedFile.MimeType)
	c.Header("Content-Disposition", fmt.Sprintf("inline; filename=%s", uploadedFile.Name))
	c.Status(http.StatusOK)
	c.Writer.Write(content)
}
