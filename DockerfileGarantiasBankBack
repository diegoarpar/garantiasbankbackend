FROM openjdk:8
COPY ./target/GarantiasBank-1.0-SNAPSHOT.jar app.jar
COPY garantiasBankBack.yml config.yml
WORKDIR /
CMD ["java", "-jar", "app.jar", "server", "config.yml"]