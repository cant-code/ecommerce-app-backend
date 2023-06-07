FROM eclipse-temurin:17.0.7_7-jre-alpine
COPY target/ecommerceapp-0.0.1-SNAPSHOT.jar ecommerceapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./ecommerceapp.jar"]
