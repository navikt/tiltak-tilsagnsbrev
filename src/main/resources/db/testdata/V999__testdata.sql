insert into public.tilsagn_under_behandling (cid, opprettet, tilsagnsbrev_id, retry, datafeil, mappet_fra_arena, journalpost_id, altinn_referanse, behandlet, json, pdf) values ('123e4567-e89b-12d3-a456-426655440000', now(), 222, 0, false, true, null, null, false, '{"tilsagnNummer":{"aar":2019,"loepenrSak":511140,"loepenrTilsagn":2},"tilsagnDato":"2019-12-02","periode":{"fraDato":"2019-12-15","tilDato":"2019-12-31"},"tiltakKode":"MIDLONTIL","tiltakNavn":"Midlertidig lønnstilskudd","administrasjonKode":"IND","refusjonfristDato":"2020-02-29","tiltakArrangor":{"arbgiverNavn":"Linesøy og Langgangen Regnskap","landKode":"NO","postAdresse":null,"postNummer":"3060","postSted":"SVELVIK","orgNummerMorselskap":910825585,"orgNummer":910825585,"kontoNummer":"86014979605","maalform":"NO"},"totaltTilskuddbelop":3200,"valutaKode":"NOK","tilskuddListe":[{"tilskuddType":"Lønnstilskudd","tilskuddBelop":3200,"visTilskuddProsent":true,"tilskuddProsent":60}],"deltaker":{"fodselsnr":"16047844235","fornavn":"MARTE STRAND","etternavn":"KVALØ","landKode":"NO","postAdresse":"Nedre Toppenhaug 186","postNummer":"1353","postSted":"BÆRUMS VERK"},"antallDeltakere":null,"antallTimeverk":null,"navEnhet":{"navKontor":"0219","navKontorNavn":"NAV Bærum","postAdresse":"Postboks 40","postNummer":"1300","postSted":"SANDVIKA","telefon":"55553333","faks":"67257601"},"beslutter":{"fornavn":"Kristina","etternavn":"Åsberg"},"saksbehandler":{"fornavn":"Kristina","etternavn":"Åsberg"},"kommentar":"safdaf"}', null);


-- insert into tilsagn_logg (id, tilsagnsbrev_id, tidspunkt_lest) values ('123e4567-e89b-12d3-a456-426655440000', 16, now());