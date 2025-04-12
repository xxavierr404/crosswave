FROM maven:latest AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD ./crosswave-auth $HOME
ADD ./crosswave-auth-openapi $HOME/crosswave-auth-openapi
ADD ./crosswave-auth-jwt-lib $HOME/crosswave-auth-jwt-lib
ADD ./crosswave-kafka-events-lib $HOME/crosswave-kafka-events-lib

RUN cd $HOME/crosswave-auth-openapi && mvn clean install
RUN cd $HOME/crosswave-auth-jwt-lib && mvn clean install
RUN cd $HOME/crosswave-kafka-events-lib && mvn clean install
RUN mvn clean package -DskipTests

FROM openjdk:21
COPY --from=build /usr/app/target/crosswave-auth-0.0.1.jar /app/runner.jar
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]