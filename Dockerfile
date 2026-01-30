FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

COPY gradlew build.gradle settings.gradle /app/
COPY gradle /app/gradle
RUN ./gradlew --version

COPY src /app/src
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
