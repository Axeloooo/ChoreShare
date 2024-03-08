FROM openjdk:17-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar event-service.jar
EXPOSE 8083
CMD ["java", "-jar", "event-service.jar"]
