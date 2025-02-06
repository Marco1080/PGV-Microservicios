# Compilamos la APP con Maven
FROM maven:3.8.5-openjdk-17 as builder

# Nos movemos a /app
WORKDIR /app

# Copiamos el pom
COPY pom.xml ./
COPY src ./src

# Generamos el jam
RUN mvn clean package -DskipTests

# Instalamos JDK
FROM openjdk:17-jdk-slim

# Nos movemos a /app
WORKDIR /app

# Copiamos el JAR
COPY --from=builder /app/target/*.jar app.jar

# Abrimos el puerto 8080
EXPOSE 9090

# Lanzamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
