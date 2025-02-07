FROM openjdk:21-jdk-slim as build
WORKDIR /app
COPY target/category-check-0.0.1-SNAPSHOT.jar /app/category-check-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "category-check-0.0.1.jar"]



#BUILD AND RUN COMMANDS
#docker build -f docker-file -t domain-checker-app:v1 .
#docker run -p 8080:8080 domain-checker-app:v1
