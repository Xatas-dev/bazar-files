# ==========================================
# Stage 1: Build the Application (Native)
# ==========================================
FROM quay.io/quarkus/ubi9-quarkus-mandrel-builder-image:jdk-21 AS builder

WORKDIR /app

# Elevate privileges only for package install
USER root
RUN microdnf install -y findutils gcc glibc-devel zlib-devel && microdnf clean all
USER quarkus

# Copy Gradle wrapper and build descriptors first for better layer caching
COPY --chown=quarkus:quarkus build.gradle.kts settings.gradle gradle.properties ./
COPY --chown=quarkus:quarkus gradle ./gradle
COPY --chown=quarkus:quarkus gradlew ./
RUN chmod +x ./gradlew

# Copy sources and build native executable
COPY --chown=quarkus:quarkus src ./src
RUN ./gradlew build -Dquarkus.native.enabled=true -Dquarkus.package.jar.enabled=false -x test --no-daemon

# ==========================================
# Stage 2: Runtime image for native binary
# ==========================================
FROM quay.io/quarkus/quarkus-micro-image:2.0

WORKDIR /application

COPY --from=builder /app/build/*-runner /application/application
RUN chmod 775 /application /application/application

EXPOSE 8080
USER 1001

ENTRYPOINT ["/application/application"]
