FROM openjdk:21

WORKDIR /app

COPY build/libs/notificationservice*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "--enable-preview", "app.jar"]
