FROM openjdk:11-jdk-slim
ADD target/spring-mongodb-graphlookup-1.0.0.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar