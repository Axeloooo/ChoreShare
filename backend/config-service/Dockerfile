FROM openjdk:17-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar config-service.jar
EXPOSE 8080
CMD ["java", "-jar", "config-service.jar"]
