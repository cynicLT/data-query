FROM openjdk:21-slim as BUILDER

WORKDIR /tmp

COPY target/data-query-*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:21-slim

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:InitialRAMPercentage=25.0 -XX:MaxRAMPercentage=80.0 -XX:+UseZGC"
ENV TMP_DIR="/usr/src/app/data/temp"

WORKDIR /usr/src/app
RUN mkdir -p ${TMP_DIR}

COPY --from=BUILDER /tmp/dependencies/ ./
COPY --from=BUILDER /tmp/spring-boot-loader/ ./
COPY --from=BUILDER /tmp/application/ ./
ENTRYPOINT java ${JAVA_OPTS} -Djava.io.tmpdir=${TMP_DIR} org.springframework.boot.loader.JarLauncher
