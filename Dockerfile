FROM navikt/java:11
COPY export_service_user.sh /init-scripts
COPY /target/tiltak-tilsagnsbrev-0.0.1-SNAPSHOT.jar app.jar
