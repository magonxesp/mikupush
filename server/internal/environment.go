package internal

import (
	"log"
	"os"
	"strconv"
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

func GetPostgresUser() string {
	return os.Getenv("POSTGRESQL_USER")
}

func GetPostgresPassword() string {
	return os.Getenv("POSTGRESQL_PASSWORD")
}

func GetPostgresDatabase() string {
	return os.Getenv("POSTGRESQL_DATABASE")
}

func GetPostgresHost() string {
	return os.Getenv("POSTGRESQL_HOST")
}

func GetPostgresPort() string {
	return os.Getenv("POSTGRESQL_PORT")
}

func GetUploadLimit() uint {
	limitStr := os.Getenv("MIKUPUSH_UPLOAD_LIMIT_BYTES")
	if limitStr == "" {
		return UnlimitedUploadSize
	}

	limit, err := strconv.ParseUint(limitStr, 10, 32)
	if err != nil {
		return UnlimitedUploadSize
	}

	return uint(limit)
}

func IsUploadSizeLimited() bool {
	limit := GetUploadLimit()
	return limit > 1
}
