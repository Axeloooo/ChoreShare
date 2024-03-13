FROM openjdk:17-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar email-service.jar
EXPOSE 8088
CMD ["java", "-jar", "email-service.jar"]