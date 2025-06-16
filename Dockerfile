FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Maven wrapper files and config first
COPY mvnw .
COPY .mvn .mvn

# Copy pom.xml and other files
COPY pom.xml .
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
