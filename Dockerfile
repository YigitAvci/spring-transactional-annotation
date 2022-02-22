FROM tomcat:10-jdk17
COPY target/transactionalAnnotation-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]