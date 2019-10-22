#!/usr/bin/env sh

if test -f /secrets/serviceuser/srvtiltak-tilsagns/username;
then
    export KAFKA_USERNAME=$(cat /secrets/serviceuser/srvtiltak-tilsagns/username)
fi

if test -f /secrets/serviceuser/srvtiltak-tilsagns/password;
then
    export KAFKA_PASSWORD=$(cat /secrets/serviceuser/srvtiltak-tilsagns/password)
fi


if [ -z "$KAFKA_USERNAME" ] ;
then
    printf "KAFKA_USERNAME er ikke satt \n";
else
    printf "KAFKA_USERNAME er ${KAFKA_USERNAME} \n" ;
fi

if [ -z "$KAFKA_PASSWORD" ] ;
then
    printf "KAFKA_PASSWORD er ikke satt \n";
else
    printf "KAFKA_PASSWORD er satt \n" ;
fi



