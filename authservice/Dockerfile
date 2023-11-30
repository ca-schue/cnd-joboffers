FROM openjdk:21

WORKDIR /app

COPY build/libs/authservice*.jar app.jar

CMD ["java", "-jar", "--enable-preview", "app.jar"]
