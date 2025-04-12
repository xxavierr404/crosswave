FROM maven:latest AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD ./crosswave-ai-adapter $HOME
ADD ./crosswave-ai-openapi $HOME/crosswave-ai-openapi

RUN cd $HOME/crosswave-ai-openapi && mvn clean install
RUN mvn clean package

FROM openjdk:21
COPY --from=build /usr/app/target/crosswave-ai-adapter-0.0.1.jar /app/runner.jar
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]