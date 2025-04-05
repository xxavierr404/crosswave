FROM maven:latest AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD ./crosswave-service-discovery $HOME

RUN mvn clean package

FROM openjdk:21
COPY --from=build /usr/app/target/crosswave-service-discovery-0.0.1.jar /app/runner.jar
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]