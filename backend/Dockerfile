FROM eclipse-temurin:21 AS build

WORKDIR /build

COPY . .

RUN --mount=type=cache,target=/root/.gradle,sharing=private ./gradlew :backend:build -x test

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /build/backend/build/libs/backend-all.jar /opt/backend.jar

EXPOSE 8080

CMD ["java", "-jar", "/opt/backend.jar"]