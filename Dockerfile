FROM ghcr.io/navikt/baseimages/temurin:17
COPY export-service-user.sh /init-scripts/03-export-service-user.sh
COPY /target/tiltak-tilsagnsbrev-*.jar app.jar