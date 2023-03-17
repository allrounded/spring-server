FROM adoptopenjdk/openjdk11

COPY ./build/libs/*.jar mogong-spring.jar

ENTRYPOINT ["java",  "-Dspring.profiles.active=default", "-jar", "/mogong-spring.jar"]
