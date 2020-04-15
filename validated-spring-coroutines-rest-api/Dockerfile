FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /validated-spring-coroutines-rest-api
COPY ./build/libs/*.jar /validated-spring-coroutines-rest-api/app.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /validated-spring-coroutines-rest-api/app.jar
