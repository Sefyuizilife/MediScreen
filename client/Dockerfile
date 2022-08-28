FROM openjdk:11
RUN mkdir "app"
WORKDIR /app
COPY target/client-0.0.1-SNAPSHOT.jar client-0.0.1.jar
ENTRYPOINT ["java", "-jar", "client-0.0.1.jar"]