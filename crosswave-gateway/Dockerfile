FROM maven:latest AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD ./crosswave-gateway $HOME
ADD ./crosswave-auth-jwt-lib $HOME/crosswave-auth-jwt-lib

RUN cd $HOME/crosswave-auth-jwt-lib && mvn clean install
RUN mvn clean package

FROM openjdk:21
COPY --from=build /usr/app/target/crosswave-gateway-0.0.1.jar /app/runner.jar
ENTRYPOINT ["java", "-jar", "/app/runner.jar"]