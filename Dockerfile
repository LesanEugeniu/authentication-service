FROM amazoncorretto:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

CMD apt-get update -y

COPY wait-for-it.sh /wait-for-it.sh

#RUN chmod +x /wait-for-it.sh

CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]
