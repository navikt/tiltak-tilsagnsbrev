#!/usr/bin/env sh

if test -f /secrets/serviceuser/srvtiltak-tilsagns/username;
then
    export tilsagnsbrev_srvuser=$(cat /secrets/serviceuser/srvtiltak-tilsagns/username)
fi

if test -f /secrets/serviceuser/srvtiltak-tilsagns/password;
then
    export tilsagnsbrev_srvpwd=$(cat /secrets/serviceuser/srvtiltak-tilsagns/password)
fi

