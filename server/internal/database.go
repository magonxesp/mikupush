package internal

import (
	"fmt"
	"log"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

var db *gorm.DB = nil

func GetDatabase() *gorm.DB {
	if db != nil {
		return db
	}

	dsn := fmt.Sprintf(
		"host=%s user=%s password=%s dbname=%s port=%s sslmode=disable",
		GetPostgresHost(),
		GetPostgresUser(),
		GetPostgresPassword(),
		GetPostgresDatabase(),
		GetPostgresPort(),
	)

	var err error
	db, err = gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Panicln("Failed to connect to the database:", err)
	}

	return db
}
