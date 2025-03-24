package main

import (
	"github.com/gin-gonic/gin"
	"mikupush.io/internal"
	"mikupush.io/internal/http"
)

func main() {
	server := gin.Default()
	http.SetupHttpServer(server)
	server.Run(internal.GetServerPort())
}
