FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/Rewards-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080


ARG PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=$PROFILE

ENTRYPOINT ["java","-jar","app.jar"]
