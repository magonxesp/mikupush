package main

import (
	"fmt"
	"log"
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

func GetFileHandler(c *gin.Context) {
	db := GetDatabase()

	fileId := c.Param("uuid")
	fileUuid, err := uuid.Parse(fileId)
	if err != nil {
		log.Println("Failed to parse file id:", err.Error())
		c.Status(http.StatusInternalServerError)
		return
	}

	var uploadedFile UploadedFile
	result := db.Where("uuid = ?", fileUuid).First(&uploadedFile)
	if result.Error != nil {
		log.Println("Uploaded file not found", fileId, "error:", result.Error.Error())
		c.Status(http.StatusNotFound)
		return
	}

	filePath := path.Join(GetDataDir(), fileId)
	content, err := os.ReadFile(filePath)
	if err != nil {
		log.Println("Failed to read content of file with id", fileId, "error:", err.Error())
		c.Status(http.StatusInternalServerError)
		return
	}

	c.Header("Content-Type", uploadedFile.MimeType)
	c.Header("Content-Disposition", fmt.Sprintf("inline; filename=%s", uploadedFile.Name))
	_, err = c.Writer.Write(content)
	if err != nil {
		log.Println("Failed to write the response body for file with id", fileId, "error:", err.Error())
		c.Status(http.StatusInternalServerError)
		return
	}

	c.Status(http.StatusOK)
}

func DeleteFileHandler(c *gin.Context) {
	db := GetDatabase()

	fileId := c.Param("uuid")
	result := db.Where("uuid = ?", fileId).Delete(&UploadedFile{})

	if result.Error != nil {
		log.Println("Failed to delete file with id", fileId, "error:", result.Error.Error())
		c.Status(http.StatusInternalServerError)
		return
	}

	filePath := path.Join(GetDataDir(), fileId)
	err := os.Remove(filePath)
	if err != nil {
		log.Println("Failed to delete file with id", fileId, "error:", err.Error())
	}

	c.Status(http.StatusOK)
}
