#!/usr/bin/env sh

if test -f /secrets/serviceuser/srvtiltak-tilsagns/username; 
then
    export KAFKA_USERNAME=$(cat /secrets/serviceuser/srvtiltak-tilsagns/username)
fi

if test -f /secrets/serviceuser/srvtiltak-tilsagns/password;
then
    export KAFKA_PASSWORD=$(cat /secrets/serviceuser/srvtiltak-tilsagns/password)
fi
