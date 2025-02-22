package main

import (
	"log"
	"os"
)

func GetDataDir() string {
	dir := os.Getenv("MIKUPUSH_DATA_DIR")
	if dir == "" {
		dir = "data"
	}

	if _, err := os.ReadDir(dir); os.IsNotExist(err) {
		err := os.MkdirAll(dir, 0755)
		if err != nil {
			log.Panicln("Can't create the data directory, and it must be created:", err)
		}
	}

	return dir
}

func GetServerPort() string {
	port := os.Getenv("MIKUPUSH_PORT")
	if port == "" {
		port = "8080"
	}

	return ":" + port
}

var PostgresUser = os.Getenv("POSTGRESQL_USER")
var PostgresPassword = os.Getenv("POSTGRESQL_PASSWORD")
var PostgresDatabase = os.Getenv("POSTGRESQL_DATABASE")
var PostgresHost = os.Getenv("POSTGRESQL_HOST")
var PostgresPort = os.Getenv("POSTGRESQL_PORT")
