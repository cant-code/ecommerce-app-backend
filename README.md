# Getting Started

### Requirements
* Java 11 (Min version 8, change the Java version in the pom.xml file accordingly)
* Relational DB - PostgreSQL
* IAM - Keycloak

### Environment Variables
Required Environment Variables:

* DB_ADDR - Provide the complete JDBC URI for the DB.
* KEYCLOAK_SERVER - The URI to the auth endpoint of the Keycloak Server.
* KEYCLOAK_SECRET - The keycloak client Secret

Optional Environment Variables:

* DB_USER - DB user username. (Default - postgres)
* DB_PASS - DB user password. (Default - mysecretpassword)
* KEYCLOAK_REALM - The keycloak realm for the application. (Default - ecommerce)
* KEYCLOAK_RESOURCE - The client created for the application. (Default - login-app)

### Steps to run
1. Clone the repo or download the source
2. Use `mvn spring-boot:run`
    or
   `mvnw spring-boot:run`
    from the base directory of the project to run the project
   
### Steps to deploy
Using Docker:
1. Build the project using `mvn package` or `mvnw package`
2. Build a docker image using:
```
docker build -t <image-name>:<tag> .
```
3. Run the image using:
```
docker run -p 8080:8080 -e DB_ADDR=<value> -e KEYCLOAK_SERVER=<value> -e KEYCLOAK_SECRET=<value> -d <image-name>:<tag>
```

Deploy jar file:
1. Build the project using `mvn package` or `mvnw package`
2. Copy the .jar generated from the /target folder to your desired location.
3. Run the program using `java -jar <filename>.jar`
