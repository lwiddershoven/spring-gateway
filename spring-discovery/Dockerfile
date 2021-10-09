# Amazon works great, even on an apple M1
FROM amazoncorretto:17

# Azul does not seem to have AMR64 builds, and running this somehow blocks.
# Or does not start at all depending on the java version (16 or 17)
# FROM azul/zulu-openjdk:17

# The default spring-boot:build-image also does not create a working image.
# Startup fails due to something.

# docker build -t leonw/test .
# docker run -p 8761:8761 -e spring.cloud.consul.host=host.docker.internal -e spring.cloud.vault.uri=http://host.docker.internal:8200 leonw/test

# For azul this was necessary to get java to start
# RUN useradd nonroot
# USER nonroot

COPY target/spring-discovery-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java", "-jar", "/app.jar"]