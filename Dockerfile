# ==========================================
# Stage 1: Build the Application (Native)
# ==========================================
FROM ghcr.io/graalvm/native-image:21 AS builder

WORKDIR /app

# Copy gradle configuration first to cache dependencies
COPY build.gradle.kts settings.gradle gradle.properties ./
COPY gradle ./gradle

COPY src ./src
RUN gradle build -Dquarkus.package.type=native -x test --no-daemon

# ==========================================
# Stage 2: Create the Runtime Image
# ==========================================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /application

# Optimize Java memory usage for containers
ENV JDK_JAVA_OPTIONS="-XX:MaxRAMPercentage=80.0 -XX:+UseStringDeduplication -XX:MaxHeapFreeRatio=10 -XX:MinHeapFreeRatio=5 -Xss512k"

# Create a non-root user for security (best practice)
RUN addgroup -S spring && adduser -S spring -G spring && chown -R spring:spring /application
USER spring:spring

# Copy native executable
COPY --from=builder /app/build/*-runner /application/application

ENTRYPOINT ["/application/application"]
