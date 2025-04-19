# Miku push

Miku Push! is a simple, fast, and open-source alternative to WeTransfer, available for Windows, macOS, and Linux. 
It doesn't require registration and allows anonymous file sharing by default, 
ensuring privacy and ease of use from the very first moment.

## Download

You can download the latest version on the releases page.

## Server

### Build

Requirements:
* Go 1.24

Go to the `server` directory and build the binary executable.

```sh
go build
```

Alternatively you can build the docker image too.

```sh
docker build . -t mikupush/server:latest
```

### Deploy

Go to the `server` directory.

#### Running the binary executable

Requirements:
* PostgreSQL 17.2

> ℹ️ Remember to create a database on the PostgreSQL server and execute the `schema.sql` script.

Create the `.env` file from the `.env.example` and configure the PostgreSQL server connection.

Once you have the server binary executable and a PostgreSQL server and the `.env` file you can run it on the command line directly.

##### Linux or macOS

```sh
chmod +x server
./server
```

You can create a `systemd` service on Linux running the server binary executable.

###### Windows

```powershell
.\server.exe
```

#### With docker compose 🐳

Create a `docker-compose.yml` file and put the following content or use the existing.

```yml
services:
  server:
    image: magonx/mikupush-server:latest
    ports:
      - "8080:8080"
    volumes:
      - ./data:/server/data
    env_file:
      - ./.env
  postgres:
    image: postgres:17.2
    ports:
      - "5432:5432"
    volumes:
      - postgres:/var/lib/postgresql/data
    environment:
      POSTGRES_USER: miku_push
      POSTGRES_PASSWORD: miku_push
      POSTGRES_DB: miku_push

volumes:
  postgres:
```

Create the `.env` file from the `.env.example` and configure the PostgreSQL server connection.

And then run the containers with docker compose.

```sh
docker compose up -d
```

> ℹ️ You can use the same content of the `docker-compose.yml` for create a stack in docker swarm.

## App

### Build

Requirements:
* Node 22.14 LTS

Go to the `app` directory and install the dependencies.

```sh
npm install
```

> ℹ️ You can change the server url editing the `VITE_SERVER_BASE_URL` environment variable 
> on the `.env` file for the production build or `.env.development` file for the dev build.

Build source code in production mode.

```sh
npm run build:prod
```

For debugging you can build the dev version.

```sh
npm run build:dev
```

Then you can build the application binaries.

```sh
npm run package
```

You can find the binaries on the `target` directory.
