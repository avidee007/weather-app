FROM eclipse-temurin:17-jdk-jammy  AS base
EXPOSE 8080
RUN addgroup --system spring && adduser --system spring --ingroup spring
WORKDIR /app
COPY . ./
RUN ./gradlew clean build

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=base /app/build/libs/*.jar /app/weather-app.jar
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
ENTRYPOINT ["java","-jar","weather-app.jar"]