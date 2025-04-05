FROM maven:latest AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD ./crosswave-music $HOME
ADD ./crosswave-music-openapi $HOME/crosswave-music-openapi

RUN cd $HOME/crosswave-music-openapi && mvn clean install
RUN mvn clean package

FROM openjdk:21
COPY --from=build /usr/app/target/crosswave-music-0.0.1.jar /app/runner.jar
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]