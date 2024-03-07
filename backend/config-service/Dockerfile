FROM openjdk:17 as build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/*.jar /app/*.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/*.jar" ]