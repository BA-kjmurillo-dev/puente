FROM openjdk:17
ADD target/puente-0.0.1-SNAPSHOT.jar puenteremesas.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/puenteremesas.jar"]