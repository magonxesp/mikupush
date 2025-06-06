FROM golang:1.24-alpine AS builder

WORKDIR /build

COPY . .

RUN go build -o mikupush-server

FROM golang:1.24-alpine

WORKDIR /server

COPY --from=builder /build/assets assets
COPY --from=builder /build/mikupush-server mikupush-server

CMD ["/server/mikupush-server"]
