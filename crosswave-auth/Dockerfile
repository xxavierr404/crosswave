FROM maven:latest AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD ./crosswave-auth $HOME
ADD ./crosswave-auth-openapi $HOME/crosswave-auth-openapi

RUN cd $HOME/crosswave-auth-openapi && mvn install
RUN mvn package

FROM openjdk:21
COPY --from=build /usr/app/target/crosswave-auth-0.0.1.jar /app/runner.jar
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]