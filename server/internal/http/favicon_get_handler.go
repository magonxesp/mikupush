package http

import (
	"log"
	"net/http"
	"os"

	"github.com/gin-gonic/gin"
)

func FaviconGetHandler(context *gin.Context) {
	content, err := os.ReadFile("assets/icon.ico")
	if err != nil {
		log.Println("Failed to read assets/icon.ico", err)
		context.Status(http.StatusInternalServerError)
		return
	}

	_, err = context.Writer.Write(content)
	if err != nil {
		log.Println("Failed to write the response body for favicon", err)
		context.Status(http.StatusInternalServerError)
		return
	}

	context.Header("Content-Type", "image/vnd.microsoft.icon")
	context.Status(http.StatusOK)
}
