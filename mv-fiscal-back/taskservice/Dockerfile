FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x gradlew
RUN ./gradlew build -x test

ENTRYPOINT ["java", "-jar", "build/libs/*.jar"]