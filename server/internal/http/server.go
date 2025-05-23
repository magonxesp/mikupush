package http

import (
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"time"
)

func SetupHandlers(engine *gin.Engine) {
	engine.POST("/api/file", FileCreateHandler)
	engine.POST("/api/file/:uuid/upload", FileUploadHandler)
	engine.DELETE("/api/file/:uuid", FileDeleteHandler)
	engine.GET("/u/:uuid", FileGetContentHandler)
	engine.GET("/health", HealthGetHandler)
}

func SetupCORS(engine *gin.Engine) {
	engine.Use(cors.New(cors.Config{
		AllowOrigins: []string{"http://localhost:5173"},
		AllowHeaders: []string{
			"Origin",
			"X-File-Id",
			"X-File-Name",
			"Content-Type",
			"Accept",
		},
		AllowMethods: []string{
			"POST",
			"GET",
			"OPTIONS",
			"DELETE",
		},
		ExposeHeaders:    []string{"Content-Length"},
		AllowCredentials: true,
		MaxAge:           12 * time.Hour,
	}))
}

func SetupHttpServer(engine *gin.Engine) {
	SetupCORS(engine)
	SetupHandlers(engine)
}
