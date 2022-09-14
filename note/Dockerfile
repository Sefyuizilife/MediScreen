FROM openjdk:11

WORKDIR /app

COPY . /app/note/

RUN cd note && sed -i 's/\r$//' mvnw && ./mvnw clean package && mv target/note-*-SNAPSHOT.jar ../note.jar && rm -rf /app/note

ENTRYPOINT ["java", "-jar", "note.jar"]