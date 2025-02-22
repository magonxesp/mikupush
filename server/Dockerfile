FROM golang:1.24.0-alpine

WORKDIR /server

COPY . .

RUN go build -o mikupush-server

CMD ["/server/mikupush-server"]