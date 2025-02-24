package main

import (
	"github.com/gin-gonic/gin"
)

func main() {
	server := gin.Default()

	server.POST("/upload", UploadHandler)
	server.GET("/:uuid", GetFileHandler)
	server.DELETE("/:uuid", DeleteFileHandler)

	server.Run(GetServerPort())
}
