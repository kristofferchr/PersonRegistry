
FROM amazoncorretto:21-alpine-jdk
MAINTAINER kristofferchr.dev

COPY build/libs/backend-0.0.1-SNAPSHOT.jar backend-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/backend-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080

