FROM openjdk:11

WORKDIR /app

COPY . /app/client/

RUN cd client && sed -i 's/\r$//' mvnw && ./mvnw clean package && mv target/client-*-SNAPSHOT.jar ../client.jar && rm -rf /app/client

ENTRYPOINT ["java", "-jar", "client.jar"]