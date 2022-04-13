FROM adoptopenjdk/openjdk11:latest
MAINTAINER rnozdrachev
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} employee-project.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "employee-project.jar"]