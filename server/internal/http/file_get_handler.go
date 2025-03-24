package http

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"log"
	"mikupush.io/internal/service"
	"net/http"
)

func FileGetContentHandler(context *gin.Context) {
	fileId := context.Param("uuid")
	fileContent, err := service.GetFileContents(fileId)
	if err != nil {
		log.Printf("failed getting file contents: %v", err)
		context.Status(http.StatusInternalServerError)
		return
	}

	if fileContent == nil {
		context.Status(http.StatusNotFound)
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
