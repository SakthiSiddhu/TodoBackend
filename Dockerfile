# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-alpine

# Expose the port that the application will run on
EXPOSE 8080

# Copy the application JAR file (or other relevant files) to the container
ADD target/todo.jar todo.jar

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/todo.jar"]
