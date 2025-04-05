FROM golang:1.24.0-alpine AS builder

WORKDIR /build

COPY . .

RUN go mod tidy
RUN go build -o mikupush-server

FROM golang:1.24.0-alpine

WORKDIR /server

COPY --from=builder /build/mikupush-server mikupush-server

CMD ["/server/mikupush-server"]
