package main

import (
	"github.com/gin-gonic/gin"
)

func main() {
	server := gin.Default()

	server.POST("/upload", UploadHandler)
	server.GET("/:uuid", FileHandler)

	server.Run(GetServerPort())
}
