FROM openjdk:11-jre-slim

ENV APPLICATION=mdm-objects-service
ENV VERSION=0.1

LABEL com.zyfra.${APPLICATION}="ECP Hierarchy Adapter"
LABEL version="$VERSION"

RUN groupadd -g 1000 $APPLICATION && \
    useradd -r -u 1000 -g $APPLICATION $APPLICATION && \
    mkdir -p /opt/$APPLICATION && \
    chown 1000:1000 /opt/$APPLICATION

EXPOSE 8097
USER 1000
WORKDIR /opt/$APPLICATION

COPY --chown=1000:1000 /build/libs/${APPLICATION}-${VERSION}.jar ./

ENTRYPOINT ["sh", "-c", "java $JAVA_APP_OPTS -jar ${APPLICATION}-${VERSION}.jar"]