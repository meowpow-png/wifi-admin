FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

COPY build/libs/wifi-admin-api-*.jar application.jar

RUN jar -xf application.jar

RUN jdeps \
    --ignore-missing-deps \
    --recursive \
    --multi-release 21 \
    --print-module-deps \
    --class-path 'BOOT-INF/lib/*' \
    BOOT-INF/classes \
    > modules.txt

RUN jlink \
    --module-path "${JAVA_HOME}/jmods" \
    --add-modules "$(cat modules.txt)" \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --compress=2 \
    --output /runtime

FROM alpine:3.23

WORKDIR /app

COPY --from=builder /runtime /opt/java
COPY build/libs/wifi-admin-api-*.jar application.jar

EXPOSE 8081

HEALTHCHECK \
    --interval=30s \
    --timeout=5s \
    --start-period=20s \
    --retries=3 \
    CMD wget -qO- http://localhost:8082/actuator/health/platform > /dev/null || exit 1

ENTRYPOINT ["/opt/java/bin/java", "-jar", "application.jar"]
