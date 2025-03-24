package http

import (
	"github.com/gin-gonic/gin"
	"log"
	"mikupush.io/internal/service"
	"net/http"
)

func FileDeleteHandler(context *gin.Context) {
	uuid := context.Param("uuid")
	err := service.DeleteFile(uuid)
	if err != nil {
		log.Println("failed deleting file:", err)
		context.Status(http.StatusBadRequest)
	}
}
