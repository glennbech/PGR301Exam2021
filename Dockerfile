# Build
FROM maven:3.6-jdk-11 AS build
WORKDIR /app 
# Get info from POM and src code
COPY pom.xml /app/
COPY src /app/src/
# Package app
RUN mvn clean package -DskipTests

# Run
FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /app
# Copy build files
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
# Run file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]