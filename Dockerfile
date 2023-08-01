FROM openjdk:19

ARG JAR_FILE=QLKhachsan-V1.0.jar

ADD ${JAR_FILE} hotel-management.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","hotel-management.jar"]
