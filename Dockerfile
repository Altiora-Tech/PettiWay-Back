# ---------- Build Stage ----------
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copiamos el proyecto
COPY . .

# Hacemos el wrapper de Maven ejecutable (si existe)
RUN chmod +x mvnw

# Build de la app (skip tests para agilizar)
RUN ./mvnw clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el JAR generado en el stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponemos puerto de la app
EXPOSE 8080

# Entry point
ENTRYPOINT ["java","-jar","/app/app.jar"]
