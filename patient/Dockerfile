FROM openjdk:11

WORKDIR /app

COPY . /app/patient/

RUN cd patient && sed -i 's/\r$//' mvnw && ./mvnw clean package && mv target/patient-*-SNAPSHOT.jar ../patient.jar && rm -rf /app/patient

ENTRYPOINT ["java", "-jar", "patient.jar"]