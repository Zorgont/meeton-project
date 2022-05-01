FROM adoptopenjdk:15-jre-hotspot
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]