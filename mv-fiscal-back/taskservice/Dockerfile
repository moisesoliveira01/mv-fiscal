FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x gradlew && ./gradlew bootJar -x test

RUN ls -la build/libs/

ENTRYPOINT ["java", "-jar", "build/libs/taskservice-0.0.1-SNAPSHOT.jar"]