FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /validated-spring-rest-api
COPY ./build/libs/*.jar /validated-spring-rest-api/app.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /validated-spring-rest-api/app.jar
