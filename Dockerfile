# --- Etapa 1: Build ---
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copia solo lo necesario para resolver dependencias primero (cache de Docker)
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# Copia el codigo y compila
COPY src src
RUN ./gradlew clean bootJar -x test --no-daemon

# --- Etapa 2: Runtime ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Usuario no-root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081

# Opciones JVM para contenedores (memoria adaptativa)
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
