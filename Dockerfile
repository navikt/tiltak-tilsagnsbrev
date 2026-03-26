FROM ghcr.io/navikt/baseimages/temurin:21
COPY export-service-user.sh /init-scripts/03-export-service-user.sh
COPY /target/tiltak-tilsagnsbrev-*.jar app.jar
