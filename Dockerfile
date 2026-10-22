# syntax=docker/dockerfile:1.7

# Этап 1: Сборка
FROM gradle:9.4-jdk21 AS build
WORKDIR /app

COPY . .

ARG MODULE_NAME

RUN --mount=type=cache,target=/home/gradle/.gradle/caches,sharing=locked \
    gradle :${MODULE_NAME}:bootJar -x test --no-daemon

# Этап 2: Запуск
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG MODULE_NAME

RUN addgroup -S app && adduser -S app -G app

COPY --from=build /app/${MODULE_NAME}/build/libs/*.jar app.jar

USER app

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java", "-jar", "app.jar"]
