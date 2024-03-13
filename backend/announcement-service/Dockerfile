FROM openjdk:17-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar announcement-service.jar
EXPOSE 8085
CMD ["java", "-jar", "announcement-service.jar"]