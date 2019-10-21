FROM navikt/java:11
COPY export-service-user.sh /init-scripts/03-export-service-user.sh
COPY /target/tiltak-tilsagnsbrev-0.0.1-SNAPSHOT.jar app.jar
