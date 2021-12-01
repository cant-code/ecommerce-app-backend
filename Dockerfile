FROM openjdk:11-jdk-slim
COPY target/ecommerceapp-0.0.1-SNAPSHOT.jar ecommerceapp.jar
ENTRYPOINT ["java", "-jar", "./ecommerceapp.jar"]