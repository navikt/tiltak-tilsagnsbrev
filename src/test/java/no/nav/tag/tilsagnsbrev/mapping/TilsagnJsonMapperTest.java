package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.junit.Test;

import static org.junit.Assert.*;

public class TilsagnJsonMapperTest {

    TilsagnJsonMapper tilsagnJsonMapper = new TilsagnJsonMapper();

    @Test
    public void mapperTilsagnTilJson() {
        String requestJson = Testdata.hentJsonFil();

        Tilsagn tilsagn = tilsagnJsonMapper.jsonTilTilsagn(requestJson);

        assertEquals("tilsagnNummer.aar", "2019", tilsagn.getTilsagnNummer().getAar());
        assertEquals("tilsagnNummer.loepenrSak", "319383", tilsagn.getTilsagnNummer().getLoepenrSak());
        assertEquals("tilsagnNummer.loepenrTilsagn", "1", tilsagn.getTilsagnNummer().getLoepenrTilsagn());

        assertEquals("tilsagnDato", "2019-09-25", tilsagn.getTilsagnDato());
        assertEquals("tiltakKode", "ENKFAGYRKE", tilsagn.getTiltakKode());
        assertEquals("tiltakNavn", "Enkeltplass Fag- og yrkesopplæring VGS og høyere yrkesfaglig utdanning", tilsagn.getTiltakNavn());
        assertEquals("administrasjonKode", "IND", tilsagn.getAdministrasjonKode());

        assertEquals("periode.fraDato", "2019-09-14", tilsagn.getPeriode().getFraDato());
        assertEquals("periode.tilDato", "2019-12-31", tilsagn.getPeriode().getTilDato());

        assertEquals("arbgiverNavn", "TREIDER KOMPETANSE AS", tilsagn.getTiltakArrangor().getArbgiverNavn());
        assertEquals("landKode", "NO", tilsagn.getTiltakArrangor().getLandKode());
        assertEquals("postAdresse", "Nedre Vollgate 8", tilsagn.getTiltakArrangor().getPostAdresse());
        assertEquals("postNummer", "0158", tilsagn.getTiltakArrangor().getPostNummer());
        assertEquals("postSted", "OSLO", tilsagn.getTiltakArrangor().getPostSted());
        assertEquals("orgNummerMorselskap", "920053106", tilsagn.getTiltakArrangor().getOrgNummerMorselskap());
        assertEquals("orgNummer", "920130283", tilsagn.getTiltakArrangor().getOrgNummer());
        assertEquals("kontoNummer", "32600596984", tilsagn.getTiltakArrangor().getKontoNummer());
        assertEquals("maalform", "NO", tilsagn.getTiltakArrangor().getMaalform());

        assertEquals("totaltTilskuddbelop", "59300", tilsagn.getTotaltTilskuddbelop());
        assertEquals("valutaKode", "NOK", tilsagn.getValutaKode());

        assertTrue("tilskuddListe.TilskuddType", tilsagn.getTilskuddListe()
                .stream()
                .allMatch(tilskudd -> tilskudd.getTilskuddType().equals("Drift")));

        assertTrue("tilskuddListe.tilskuddBelop", tilsagn.getTilskuddListe()
                .stream()
                .anyMatch(tilskudd ->
                        tilskudd.getTilskuddBelop().equals("48000")
                                || tilskudd.getTilskuddBelop().equals("1800")
                                || tilskudd.getTilskuddBelop().equals("9500")));


        assertEquals("deltaker.fodselsnr", "0101011990", tilsagn.getDeltaker().getFodselsnr());
        assertEquals("deltaker.fornavn", "OLA", tilsagn.getDeltaker().getFornavn());
        assertEquals("deltaker.etternavn", "NORDMANN", tilsagn.getDeltaker().getEtternavn());
        assertEquals("deltaker.landKode", "NO", tilsagn.getDeltaker().getLandKode());
        assertEquals("deltaker.postAdresse", "Veien 1", tilsagn.getDeltaker().getPostAdresse());
        assertEquals("deltaker.postNummer", "2013", tilsagn.getDeltaker().getPostNummer());
        assertEquals("deltaker.postSted", "SKJETTEN", tilsagn.getDeltaker().getPostSted());

        assertEquals("antallDeltakere", "1", tilsagn.getAntallDeltakere());
        assertEquals("antallTimeverk", "120", tilsagn.getAntallTimeverk());

        assertEquals("navEnhet.navKontor", "0231", tilsagn.getNavEnhet().getNavKontor());
        assertEquals("navEnhet.navKontorNavn", "NAV Skedsmo", tilsagn.getNavEnhet().getNavKontorNavn());
        assertEquals("navEnhet.postAdresse", "Postboks 294", tilsagn.getNavEnhet().getPostAdresse());
        assertEquals("navEnhet.postNummer", "2001", tilsagn.getNavEnhet().getPostNummer());
        assertEquals("navEnhet.postSted", "LILLESTRØM", tilsagn.getNavEnhet().getPostSted());
        assertEquals("navEnhet.telefon", "55553333", tilsagn.getNavEnhet().getTelefon());
        assertNull("navEnhet.faks", tilsagn.getNavEnhet().getFaks());

        assertEquals("beslutter.fornavn", "Bjarne", tilsagn.getBeslutter().getFornavn());
        assertEquals("beslutter.etternavn", "Beslutter", tilsagn.getBeslutter().getEtternavn());

        assertEquals("saksbehandler.fornavn", "Stine", tilsagn.getSaksbehandler().getFornavn());
        assertEquals("saksbehandler.etternavn", "Saksbehandler", tilsagn.getSaksbehandler().getEtternavn());

        assertEquals("kommentar", "Ingen", tilsagn.getKommentar());
    }
}
