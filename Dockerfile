# ---- Build ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Baixa dependências primeiro (cache de camadas)
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

COPY src src
RUN ./mvnw -B -q package -DskipTests

# ---- Runtime ----
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN useradd --system --create-home sapo && mkdir -p /app/uploads && chown sapo /app/uploads
USER sapo

COPY --from=build /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod \
    UPLOADS_DIR=/app/uploads \
    JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
