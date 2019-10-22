#!/usr/bin/env sh

if test -f /secrets/serviceuser/dev/srvtiltak-tilsagns/username;
then
    export KAFKA_USERNAME=H$(cat /secrets/serviceuser/dev/srvtiltak-tilsagns/username)
fi

if test -f /secrets/serviceuser/dev/srvtiltak-tilsagns/password;
then
    export KAFKA_PASSWORD=$(cat /secrets/serviceuser/dev/srvtiltak-tilsagns/password)
fi

if [ -z "$KAFKA_USERNAME" ] ;
then
    echo "KAFKA_USERNAME er ikke satt";
else
    echo "KAFKA_USERNAME er satt" ;
fi



