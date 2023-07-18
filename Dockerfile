FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/QLKhachsan-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} hotel-management.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","hotel-management.jar"]
