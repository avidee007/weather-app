# Weather App

## Introduction

This is a fully functional, secured weather application that uses publicly
exposed APIs to fetch weather of the valid USA postal code. This weather service uses
a secure api key generated at [WeatherAPI](https://www.weatherapi.com/) to fetch current weather data.

### Functional Features:

1. Register to application with unique username and strong password.
2. Get a weather report of any valid USA postal/Zip code.
3. Get history of all requested weather reports for any given userName.
4. Get history of all requested weather reports for any given postal code.
5. Deactivate any existing user.

### Non-Functional Features:

1. **Security:**
    * Signup API prohibits duplicate username creation.
    * Strong password policy is enforced. Password must contain One number, One uppercase letters, One lowercase
      letters, One non-alpha numeric number and minimum 8–16 characters in length.
    * All endpoints are secured with username/password authentication except Signup API it is for user registration.
    * Role bases authorization is implemented for all secured endpoints.
    * Deactivate API is only accessible for **ADMIN** user. Admin user is automatically created
      once application runs first time through a liquibase script using encrypted values.
    * **ADMIN** credentials will be provided on reference.
    * Other APIs are assessable for both **USER** and **ADMIN** user.

2. **Scalability:**
    * Docker images can be created with Docker support.
    * Horizontal scaling using container orchestration tools like Kubernetes.
   
3. **High Availability:**
    * High availability using container orchestration tools like Kubernetes.

### Tech Stack and Frameworks:

This microservice was build using Spring Boot with the following frameworks:

* **JDK 17:** Used as JVM language.
* **Spring Web:** Used for building RESTFul API for user management and weather operations.
* **Spring Data JPA:** Used for database access and database operations.
* **Liquibase:** Used for database migration.
* **PostgresSQL:** As RDBMS to store user and weather entities.
* **Spring Security:** Used for securing exposed REST API by implementing authentication and authorization.
* **Spring Validation:** Used for implement bean validation for user inputs.
* **Springdoc OpenApi:** Used to implement live API documentation using Swagger.
* **JUnit 5:** Used to implement unit test cases.
* **Mockito:** Used for mocking external dependencies in unit test cases.
* **SpringBootTest:** Used for complete integration testing.

### How to run the project

**NOTE:** WeatherApi api key needs to be configured to application.yaml file to fetch data from public weather api.
Complete steps are mentioned in [How-to-run-guide](docs/How-to-Run.md).
Please follow the instructions documented in [How-to-run-guide](docs/How-to-Run.md).

### Swagger for API documentation and testing

Once application is started with any of the chosen methods from the above step.
[Click here to open Swagger UI.](http://localhost:8080/weather-app/swagger-ui/index.html#/)
This will open swagger UI which can be used to test the APIS.

Detailed steps for authentication and testing the API are mentioned in [Runbook](docs/Steps-to-test-api.md).

The Application covers all negative and edge cases of authentication, authorization and api functionalities as per my
testing. I am open to further feedback and issues, if any.