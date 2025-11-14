package main

import (
    "log"
    "net/http"

    "dev.ufadevconf/vo-go/internal/application"
    "dev.ufadevconf/vo-go/internal/infrastructure/httpapi"
    "dev.ufadevconf/vo-go/internal/infrastructure/repository"
)

func main() {
    repo := repository.NewInMemoryPickupPointRepository()
    appService := application.NewPickupPointApplicationService(repo)
    server := httpapi.NewServer(appService)

    addr := ":8080"
    log.Printf("starting HTTP server on %s", addr)
    if err := http.ListenAndServe(addr, server); err != nil {
        log.Fatalf("server stopped: %v", err)
    }
}
