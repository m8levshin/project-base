FROM azul/zulu-openjdk:17-jre
RUN mkdir /app
WORKDIR /app
COPY ./build/libs/*-all.jar service.jar
ENTRYPOINT ["java", "-jar", "service.jar"]