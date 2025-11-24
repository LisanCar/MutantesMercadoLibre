# ========================================
# ETAPA 1: BUILD (Compilación)
# ========================================
# Usamos una imagen oficial de JDK 21 basada en Alpine (ligera)
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiar todo el código fuente
COPY . .

# Dar permisos de ejecución al wrapper de Gradle
RUN chmod +x ./gradlew

# Compilar el proyecto.
# Usamos el wildcard (*) en el COPY final para no depender del nombre exacto del jar
RUN ./gradlew bootJar --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# ========================================
# Usamos la versión JRE (solo ejecución) de Java 21 para ahorrar espacio
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Documentamos el puerto
EXPOSE 8080

# Copiamos el JAR generado.
# Usamos *.jar para que tome cualquier nombre que genere Gradle (0.0.1-SNAPSHOT)
COPY --from=build /app/build/libs/*.jar app.jar

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]