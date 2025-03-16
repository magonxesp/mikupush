package main

import (
	"io"
	"log"
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
		log.Println("Missing X-File-Id header")
		c.Status(http.StatusBadRequest)
		return
	}

	fileUuid, err := uuid.Parse(fileId)
	if err != nil {
		log.Println("Failed to parse file id:", err.Error())
		c.Status(http.StatusInternalServerError)
		return
	}

	fileName := request.Header.Get("X-File-Name")
	if fileName == "" {
		log.Println("Missing X-File-Name header")
		c.Status(http.StatusBadRequest)
		return
	}

	mimeType := request.Header.Get("Content-Type")
	if mimeType == "" {
		log.Println("Missing Content-Type header")
		c.Status(http.StatusBadRequest)
		return
	}

	file, err := os.Create(path.Join(GetDataDir(), fileId))
	if err != nil {
		log.Println("Failed creating data directory:", err.Error())
		c.Status(http.StatusInternalServerError)
		return
	}

	defer file.Close()

	// Copiar el cuerpo del request directamente al archivo
	_, err = io.Copy(file, request.Body)
	if err != nil {
		log.Println("Failed saving uploaded file:", err.Error())
		c.Status(http.StatusInternalServerError)
		return
	}

	uploadedFile := UploadedFile{
		Uuid:       fileUuid,
		Name:       fileName,
		MimeType:   mimeType,
		Size:       uint(request.ContentLength),
		UploadedAt: time.Now(),
	}

	result := db.Create(&uploadedFile)
	if result.Error != nil {
		log.Println("Failed saving uploaded file data to database:", result.Error.Error())
		c.Status(http.StatusInternalServerError)
		return
	}

	c.Status(http.StatusCreated)
}
