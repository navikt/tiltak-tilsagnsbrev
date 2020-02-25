insert into public.tilsagn_under_behandling (cid, opprettet, tilsagnsbrev_id, retry, datafeil, mappet_fra_arena,
                                             journalpost_id, altinn_referanse, behandlet, json, pdf)
values ('123e4567-e89b-12d3-a456-426655440000',
        now(),
        222,
        0,
        false,
        true,
        null,
        null,
        false,
        '{"tilsagnNummer":{"aar":2019,"loepenrSak":511140,"loepenrTilsagn":2},"tilsagnDato":"2019-12-02","periode":{"fraDato":"2019-12-15","tilDato":"2019-12-31"},"tiltakKode":"MIDLONTIL","tiltakNavn":"Midlertidig lønnstilskudd","administrasjonKode":"IND","refusjonfristDato":"2020-02-29","tiltakArrangor":{"arbgiverNavn":"Tull og Tøys Regnskap","landKode":"NO","postAdresse":null,"postNummer":"1333","postSted":"Andeby","orgNummerMorselskap":999999999,"orgNummer":99999998,"kontoNummer":"123456789012","maalform":"NO"},"totaltTilskuddbelop":3200,"valutaKode":"NOK","tilskuddListe":[{"tilskuddType":"Lønnstilskudd","tilskuddBelop":3200,"visTilskuddProsent":true,"tilskuddProsent":60}],"deltaker":{"fodselsnr":"12529946056","fornavn":"DONALD","etternavn":"DUCK","landKode":"NO","postAdresse":"Andebygata 1","postNummer":"1333","postSted":"Andeby"},"antallDeltakere":null,"antallTimeverk":null,"navEnhet":{"navKontor":"0001","navKontorNavn":"NAV Andeby","postAdresse":"Andebyveien 5","postNummer":"1333","postSted":"Andeby","telefon":"55553333","faks":"12345678"},"beslutter":{"fornavn":"Hans","etternavn":"Nilsen"},"saksbehandler":{"fornavn":"Nils","etternavn":"Hansen"},"kommentar":"safdaf"}',
        null);


-- insert into tilsagn_logg (id, tilsagnsbrev_id, tidspunkt_lest) values ('123e4567-e89b-12d3-a456-426655440000', 16, now());
