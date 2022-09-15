FROM openjdk:11

WORKDIR /app

COPY . /app/report/

RUN cd report && sed -i 's/\r$//' mvnw && ./mvnw clean package && mv target/report-*-SNAPSHOT.jar ../report.jar && rm -rf /app/report

ENTRYPOINT ["java", "-jar", "report.jar"]