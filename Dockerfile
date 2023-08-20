### build statge ###
FROM openjdk:11

# copy & build
COPY . .
RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix gradlew
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# unpack jar
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]