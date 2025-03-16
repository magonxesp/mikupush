package main

import (
	"time"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
)

func main() {
	server := gin.Default()

	server.Use(cors.New(cors.Config{
		AllowOrigins: []string{"http://localhost:5173"},
		AllowHeaders: []string{
			"Origin",
			"X-File-Id",
			"X-File-Name",
			"Content-Type",
			"Accept",
		},
		ExposeHeaders:    []string{"Content-Length"},
		AllowCredentials: true,
		MaxAge:           12 * time.Hour,
	}))

	server.POST("/upload", UploadHandler)
	server.GET("/:uuid", GetFileHandler)
	server.DELETE("/:uuid", DeleteFileHandler)

	server.Run(GetServerPort())
}
