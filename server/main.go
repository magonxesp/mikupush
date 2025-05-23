package main

import (
	"log"

	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
	"mikupush.io/server/internal"
	"mikupush.io/server/internal/http"
)

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Println("error loading .env file", err)
	}

	server := gin.Default()
	http.SetupHttpServer(server)
	server.Run(internal.GetServerPort())
}
