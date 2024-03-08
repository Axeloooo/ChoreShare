FROM openjdk:17-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar api-gateway.jar
EXPOSE 8888
CMD ["java", "-jar", "api-gateway.jar"]
