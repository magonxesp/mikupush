package http

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func HealthGetHandler(context *gin.Context) {
	context.Status(http.StatusOK)
}
