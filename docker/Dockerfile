FROM maven:3.9.7-eclipse-temurin-17 AS zeneszer
RUN apt-get update && apt-get install -y nodejs npm
WORKDIR /app
COPY src/ src/
COPY angular.json /app/
COPY pom.xml /app/
COPY package.json /app/
COPY tsconfig.json /app/
COPY tsconfig.app.json /app/
RUN mvn clean install
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/musical-instrument-shop-0.0.1-SNAPSHOT.jar", "--server.address=0.0.0.0"]