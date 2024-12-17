# How to run the application:

**NOTE:-** WeatherApi api key needs to be configured to application.yaml file. Api key is shared one to one over the
email.
Please use the same api key and use it against **weather.api.apiKey** property.

Postgres database is required to run the application. Choose one of the options below.

## Running application with dependencies with docker compose - **(MOST RECOMMEND WAY)**

1. Build the docker image
   `docker-compose build`
2. Create and start all containers in the background
   `docker-compose up -d`
3. Check container status with `docker-compose ps`. They should all have status 'Up'
4. The Application is running with all required dependencies.

## Running application with Gradle or from Intellij and dependencies with docker compose

1. Run the database
   `docker-compose up -d databse`
2. (Optional) Run adminer database viewer tool
   `docker-compose up -d adminer`
3. Run the application with either:
    1. gradle, providing datasource configuration explicitly
       `./gradlew bootRun`
    2. Intellij, providing environment variables in the launch configuration
        1. SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/postgres
        2. SPRING_DATASOURCE_USERNAME=postgres
        3. SPRING_DATASOURCE_PASSWORD=postgres

## Running without a Postgres docker image

Install Postgres manually and have it running on localhost on port 5432. There has to be user "postgres"
with password "postgres" with full access to public schema. Then run gradle wrapper `./gradlew bootRun`.