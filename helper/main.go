package main

import (
	"log"
	"net"
	"os"
	"os/exec"
	"time"
)

func main() {
	filePath := os.Args[1]
	log.Println("Launching file upload request for", filePath)

	dial := net.Dialer{Timeout: 1 * time.Second}
	conn, err := dial.Dial("tcp", "127.0.0.1:4040")

	if err != nil {
		log.Println("Socket not opened trying to delegate to main Program:", err)

		cmd := exec.Command("miku-push", filePath)
		err := cmd.Start()

		if err != nil {
			log.Println("Failed executing miku-push executable:", err)
			return
		}

		return
	}

	defer conn.Close()
	_, err = conn.Write([]byte(filePath))
	if err != nil {
		log.Println("Error sending message through socket:", err)
		return
	}

	buffer := make([]byte, 1024)
	n, err := conn.Read(buffer)
	if err != nil {
		log.Println("Error reading socket response:", err)
		return
	}

	response := string(buffer[:n])
	if response != "ok" {
		log.Fatalln("Invalid socket ack message:", string(buffer[:n]))
	}
}
