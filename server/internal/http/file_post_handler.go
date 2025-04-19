package http

import (
	"errors"
	"io"
	"log"
	"net/http"

	"mikupush.io/server/internal"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"mikupush.io/server/internal/service"
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

	if err != nil && errors.Is(err, service.ErrFileSizeExceedLimit) {
		log.Println("failed uploading file:", err)
		context.Status(http.StatusBadRequest)
		return
	}

	if err != nil {
		log.Println("failed creating file:", err)
		context.Status(http.StatusInternalServerError)
		return
	}

	context.Status(http.StatusOK)
}

func FileUploadHandler(context *gin.Context) {
	uuid := context.Param("uuid")

	var reader io.ReadCloser
	if internal.IsUploadSizeLimited() {
		reader = http.MaxBytesReader(context.Writer, context.Request.Body, int64(internal.GetUploadLimit()))
	} else {
		reader = context.Request.Body
	}

	err := service.SaveFileContents(uuid, reader)

	if err != nil && errors.Is(err, gorm.ErrRecordNotFound) {
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
