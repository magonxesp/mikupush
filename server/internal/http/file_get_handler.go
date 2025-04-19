package http

import (
	"errors"
	"fmt"
	"log"
	"net/http"

	"gorm.io/gorm"

	"github.com/gin-gonic/gin"
	"mikupush.io/server/internal/service"
)

func FileGetContentHandler(context *gin.Context) {
	fileId := context.Param("uuid")
	fileContent, err := service.GetFileContents(fileId)
	if errors.Is(err, gorm.ErrRecordNotFound) {
		context.Status(http.StatusNotFound)
		return
	}

	if err != nil {
		log.Printf("failed getting file contents: %v", err)
		context.Status(http.StatusInternalServerError)
		return
	}

	context.Header("Content-Type", fileContent.MimeType)
	context.Header("Content-Disposition", fmt.Sprintf("inline; filename=%s", fileContent.Name))

	_, err = context.Writer.Write(fileContent.Content)
	if err != nil {
		log.Println("Failed to write the response body for file with id", fileId, "error:", err.Error())
		context.Status(http.StatusInternalServerError)
		return
	}

	context.Status(http.StatusOK)
}
