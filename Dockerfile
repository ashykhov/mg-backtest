FROM openjdk:17-jdk-slim

WORKDIR /app

ARG JAR_FILE=./build/libs/backtest-service.jar

COPY ${JAR_FILE} /app/backtest-service.jar

EXPOSE 8106

ENTRYPOINT ["java","-jar","backtest-service.jar"]
