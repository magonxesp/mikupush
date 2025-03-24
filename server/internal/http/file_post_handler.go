package http

import (
	"errors"
	"github.com/gin-gonic/gin"
	"log"
	"mikupush.io/internal/service"
	"net/http"
)

func FileCreateHandler(context *gin.Context) {
	var request *service.FileCreateRequest
	err := context.ShouldBindJSON(&request)
	if err != nil {
		log.Println("failed creating file:", err)
		context.Status(http.StatusBadRequest)
		return
	}

	err = service.CreateFile(request)
	if err != nil {
		log.Println("failed creating file:", err)
		context.Status(http.StatusBadRequest)
	}
}

func FileUploadHandler(context *gin.Context) {
	uuid := context.Param("uuid")
	err := service.SaveFileContents(uuid, context.Request.Body)

	if err != nil && errors.Is(err, service.FileNotFoundError) {
		log.Println("failed uploading file:", err)
		context.Status(http.StatusBadRequest)
		return
	}

	if err != nil {
		log.Println("failed uploading file:", err)
		context.Status(http.StatusInternalServerError)
		return
	}

	context.Status(http.StatusOK)
}
