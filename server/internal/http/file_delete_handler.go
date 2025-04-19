package http

import (
	"log"
	"net/http"

	"github.com/gin-gonic/gin"
	"mikupush.io/server/internal/service"
)

func FileDeleteHandler(context *gin.Context) {
	uuid := context.Param("uuid")
	err := service.DeleteFile(uuid)
	if err != nil {
		log.Println("failed deleting file:", err)
		context.Status(http.StatusInternalServerError)
	}

	context.Status(http.StatusOK)
}
