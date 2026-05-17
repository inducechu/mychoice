# Этап 1: Сборка
FROM gradle:9.4-jdk21 AS build
WORKDIR /app

# Копируем только файлы конфигурации для кэширования зависимостей
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle/ gradle/
COPY common-proto/build.gradle.kts common-proto/
# ... скопируй так же build.gradle.kts для всех модулей ...

# Предварительно скачиваем зависимости (опционально, но ускоряет повторные билды)
# RUN gradle dependencies --no-daemon

# Теперь копируем исходники
COPY . .

ARG MODULE_NAME
# Используем системный gradle, чтобы не качать wrapper внутри контейнера
RUN gradle :${MODULE_NAME}:bootJar -x test --no-daemon

# Этап 2: Запуск
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG MODULE_NAME

# Копируем скомпилированный jar
COPY --from=build /app/${MODULE_NAME}/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
