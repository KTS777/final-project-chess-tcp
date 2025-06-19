#
# FROM maven:3.9.6-eclipse-temurin-17 AS builder
#
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
#
# RUN mvn clean package -DskipTests
#
# FROM eclipse-temurin:17-jre-alpine
#
# WORKDIR /app
#
# COPY --from=builder /app/target/chess-app-1.0-SNAPSHOT.jar app.jar
#
# ENV DB_HOST=postgres
# ENV DB_PORT=5432
# ENV DB_NAME=chessdb
# ENV DB_USER=chess
# ENV DB_PASSWORD=chesspass
#
# HEALTHCHECK --interval=30s --timeout=3s \
#   CMD curl -f http://localhost:8080/health || exit 1
#
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]