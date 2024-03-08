FROM openjdk:17-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar userhousehold-service.jar
EXPOSE 8084
CMD ["java", "-jar", "userhousehold-service.jar"]