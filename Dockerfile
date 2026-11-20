# ---------- Etapa 1: build ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Cachear dependencias de Maven
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw && ./mvnw -B dependency:go-offline

# Compilar y empaquetar (el WAR de Spring Boot es ejecutable)
COPY src src
RUN ./mvnw -B clean package -DskipTests

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

# Usuario no root
RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

COPY --from=build /app/target/devops1-*.war app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
