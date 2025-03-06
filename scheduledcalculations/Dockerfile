FROM gradle:7.5.1-jdk17 AS build
WORKDIR /home/app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN ./gradlew clean build -x test

FROM openjdk:17-jdk-slim
WORKDIR /home/app
COPY --from=build /home/app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]