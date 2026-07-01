FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/*-jar-with-dependencies.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--mode=web", "--port=8080"]
