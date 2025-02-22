package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"path"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

func UploadHandler(c *gin.Context) {
	db := GetDatabase()
	request := c.Request

	fileId := request.Header.Get("X-File-Id")
	if fileId == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Missing X-File-Id header"})
		return
	}

	fileUuid, err := uuid.Parse(fileId)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": fmt.Sprintf("Failed to parse file id: %s", err.Error())})
		return
	}

	fileName := request.Header.Get("X-File-Name")
	if fileName == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Missing X-File-Name header"})
		return
	}

	mimeType := request.Header.Get("Content-Type")
	if mimeType == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Missing Content-Type header"})
		return
	}

	file, err := os.Create(path.Join(GetDataDir(), fileId))
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	defer file.Close()

	// Copiar el cuerpo del request directamente al archivo
	_, err = io.Copy(file, request.Body)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	uploadedFile := UploadedFile{
		Uuid:       fileUuid,
		Name:       fileName,
		MimeType:   mimeType,
		Size:       uint(request.ContentLength),
		UploadedAt: time.Now(),
	}

	db.Create(&uploadedFile)

	c.Status(http.StatusCreated)
}
