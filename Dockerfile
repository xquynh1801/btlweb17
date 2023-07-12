FROM openjdk:19
EXPOSE 8080
ARG JAR_FILE=target/QLKhachsan-V1.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
