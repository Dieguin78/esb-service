# Etapa 1: Compilar con Maven
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar archivos del proyecto y compilar
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Crear la imagen de ejecución con Java 8
FROM eclipse-temurin:8-jdk-alpine

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo .jar desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
