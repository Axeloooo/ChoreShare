FROM openjdk:17-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar registry-service.jar
EXPOSE 8761
CMD ["java", "-jar", "registry-service.jar"]
