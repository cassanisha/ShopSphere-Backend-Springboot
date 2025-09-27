# Use OpenJDK 17 JRE
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy your pre-built JAR
COPY target/NotificationServiceF-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]