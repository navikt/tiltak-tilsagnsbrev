#!/usr/bin/env sh

if test -f /secrets/serviceuser/srvtiltak-tilsagns/username;
then
    export tilsagnsbrev_srvuser=$(cat /secrets/serviceuser/srvtiltak-tilsagns/username)
fi

if test -f /secrets/serviceuser/srvtiltak-tilsagns/password;
then
    export tilsagnsbrev_srvpwd=$(cat /secrets/serviceuser/srvtiltak-tilsagns/password)
fi

if [ -z "$tilsagnsbrev_srvuser" ] ;
then
    printf "tilsagnsbrev_srvuser er ikke satt \n";
else
    printf "tilsagnsbrev_srvuser er ${tilsagnsbrev_srvuser} \n" ;
fi

if [ -z "$tilsagnsbrev_srvpwd" ] ;
then
    printf "tilsagnsbrev_srvpwd er ikke satt \n";
else
    printf "tilsagnsbrev_srvpwd er satt \n" ;
fi


