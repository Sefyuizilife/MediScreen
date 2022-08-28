FROM openjdk:11
RUN mkdir "app"
WORKDIR /app
COPY target/note-0.0.1-SNAPSHOT.jar note-0.0.1.jar
ENTRYPOINT ["java", "-jar", "note-0.0.1.jar"]