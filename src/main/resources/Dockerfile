# Используем образ с JDK 17 (или нужной версии)
FROM eclipse-temurin:17-jdk-alpine

# Рабочая директория внутри контейнера
WORKDIR /app

# Копируем файл сборки и исходники (если хотим билдить в контейнере)
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
COPY src /app/src

# Собираем jar
RUN ./gradlew clean bootJar --no-daemon

# Копируем собранный jar в образ
# (Если вы билдите вне контейнера, этот шаг не нужен, тогда копируете готовый jar напрямую)
#COPY build/libs/*.jar app.jar

# Указываем команду запуска приложения
CMD ["java", "-jar", "build/libs/myapp.jar"]

# Expose порт
EXPOSE 8080
