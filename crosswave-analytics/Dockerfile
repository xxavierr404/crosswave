FROM maven:latest AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD ./crosswave-analytics $HOME
ADD ./crosswave-analytics-openapi $HOME/crosswave-analytics-openapi
ADD ./crosswave-kafka-events-lib $HOME/crosswave-kafka-events-lib

RUN cd $HOME/crosswave-analytics-openapi && mvn clean install
RUN cd $HOME/crosswave-kafka-events-lib && mvn clean install
RUN mvn clean package

FROM openjdk:21
COPY --from=build /usr/app/target/crosswave-analytics-0.0.1.jar /app/runner.jar
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]