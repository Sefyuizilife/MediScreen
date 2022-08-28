FROM openjdk:11
RUN mkdir "app"
WORKDIR /app
COPY target/report-0.0.1-SNAPSHOT.jar report-0.0.1.jar
ENTRYPOINT ["java", "-jar", "report-0.0.1.jar"]