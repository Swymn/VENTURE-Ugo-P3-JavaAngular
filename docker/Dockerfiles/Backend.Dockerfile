FROM maven:3.9.9 AS build

WORKDIR /app

COPY ./backend/pom.xml ./
COPY ./backend/src ./src

RUN mvn clean package -DskipTests

FROM openjdk:22

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]