package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.junit.Test;

import static no.nav.tag.tilsagnsbrev.Testdata.*;
import static org.junit.Assert.*;

public class TilsagnJsonMapperTest {

    TilsagnJsonMapper tilsagnJsonMapper = new TilsagnJsonMapper();
    final String requestJson = Testdata.hentFilString(JSON_FIL);

    @Test
    public void mapperTilstagnTilJson(){
        Tilsagn tilsagn = Testdata.tilsagnsbrev();
        String json = tilsagnJsonMapper.tilsagnTilPdfJson(tilsagn);
        System.out.println(json);
        assertTrue(json.contains("\"administrasjonKode\":\"" + tilsagn.getAdministrasjonKode() +"\""));
        assertTrue(json.contains("\"antallDeltakere\":\"" + tilsagn.getAntallDeltakere() +"\""));
        assertTrue(json.contains("\"antallTimeverk\":\"" + tilsagn.getAntallTimeverk() +"\""));
        assertTrue(json.contains("\"beslutter\":{\"etternavn\":\"" + tilsagn.getBeslutter().getEtternavn() +"\",\"fornavn\":\""+ tilsagn.getBeslutter().getFornavn() + "\""));
        assertTrue(json.contains("\"kommentar\":\"" + tilsagn.getKommentar() +"\""));
        assertTrue(json.contains("\"faks\":\"" + tilsagn.getNavEnhet().getFaks() +"\""));
        assertTrue(json.contains("\"navKontor\":\"" + tilsagn.getNavEnhet().getNavKontor() +"\""));

        assertTrue(json.contains("\"navKontorNavn\":\"" + tilsagn.getNavEnhet().getNavKontorNavn() +"\""));
        assertTrue(json.contains("\"postAdresse\":\"" + tilsagn.getNavEnhet().getPostAdresse() +"\""));
        assertTrue(json.contains("\"postNummer\":\"" + tilsagn.getNavEnhet().getPostNummer() +"\""));
        assertTrue(json.contains("\"postSted\":\"" + tilsagn.getNavEnhet().getPostSted() +"\""));
        assertTrue(json.contains("\"telefon\":\"" + tilsagn.getNavEnhet().getTelefon() +"\""));
        assertTrue(json.contains("\"postSted\":\"" + tilsagn.getNavEnhet().getPostSted() +"\""));
        assertTrue(json.contains("periode\":{\"fraDato\":\"01 oktober 2019\",\"tilDato\":\"31 desember 2019\"}"));
        assertTrue(json.contains("\"saksbehandler\":{\"etternavn\":\"" + tilsagn.getSaksbehandler().getEtternavn() +"\",\"fornavn\":\""+ tilsagn.getSaksbehandler().getFornavn() + "\""));
        assertTrue(json.contains("\"tilsagnDato\":\"22 oktober 2019\""));

        assertTrue(json.contains("\"aar\":\"" + tilsagn.getTilsagnNummer().getAar() +"\""));
        assertTrue(json.contains("\"loepenrSak\":\"" + tilsagn.getTilsagnNummer().getLoepenrSak() +"\""));
        assertTrue(json.contains("\"loepenrTilsagn\":\"" + tilsagn.getTilsagnNummer().getLoepenrTilsagn() +"\""));

        //TODO verifisere mer eller gjøre det vha. pdf mock
    }

    @Test
    public void mapperGoldengateJsonTilTilsagn() {
        final Tilsagn faktiskilsagn = tilsagnJsonMapper.goldengateMeldingTilTilsagn(requestJson);

        assertEquals("tilsagnNummer.aar", "2019", faktiskilsagn.getTilsagnNummer().getAar());
        assertEquals("tilsagnNummer.loepenrSak", "366023", faktiskilsagn.getTilsagnNummer().getLoepenrSak());
        assertEquals("tilsagnNummer.loepenrTilsagn", "1", faktiskilsagn.getTilsagnNummer().getLoepenrTilsagn());

        assertEquals("tilsagnDato", TILSAGNSDATO, faktiskilsagn.getTilsagnDato());
        assertEquals("tiltakKode", "BIO", faktiskilsagn.getTiltakKode());
        assertEquals("tiltakNavn", "Bedriftsintern opplæring (BIO)", faktiskilsagn.getTiltakNavn());
        assertEquals("administrasjonKode", "INST", faktiskilsagn.getAdministrasjonKode());

        assertEquals("periode.fraDato", FRA_DATO, faktiskilsagn.getPeriode().getFraDato());
        assertEquals("periode.tilDato", TIL_DATO, faktiskilsagn.getPeriode().getTilDato());

        assertEquals("arbgiverNavn", "L.S. Solland AS", faktiskilsagn.getTiltakArrangor().getArbgiverNavn());
        assertEquals("landKode", "NO", faktiskilsagn.getTiltakArrangor().getLandKode());
        assertEquals("postAdresse", "Pedersgata 110", faktiskilsagn.getTiltakArrangor().getPostAdresse());
        assertEquals("postNummer", "4014", faktiskilsagn.getTiltakArrangor().getPostNummer());
        assertEquals("postSted", "STAVANGER", faktiskilsagn.getTiltakArrangor().getPostSted());
        assertEquals("orgNummerMorselskap", "918160922", faktiskilsagn.getTiltakArrangor().getOrgNummerMorselskap());
        assertEquals("orgNummer", "973152289", faktiskilsagn.getTiltakArrangor().getOrgNummer());
        assertEquals("kontoNummer", "32010501481", faktiskilsagn.getTiltakArrangor().getKontoNummer());
        assertEquals("maalform", "NO", faktiskilsagn.getTiltakArrangor().getMaalform());

        assertEquals("totaltTilskuddbelop", "284000", faktiskilsagn.getTotaltTilskuddbelop());
        assertEquals("valutaKode", "NOK", faktiskilsagn.getValutaKode());

        assertTrue("tilskuddListe.TilskuddType", faktiskilsagn.getTilskuddListe()
                .stream()
                .anyMatch(tilskudd -> tilskudd.getTilskuddType().equals("Opplæringstilskudd")));

        assertTrue("tilskuddListe.TilskuddType", faktiskilsagn.getTilskuddListe()
                .stream()
                .anyMatch(tilskudd -> tilskudd.getTilskuddType().equals("Lønnstilskudd")));

        assertTrue("tilskuddListe.tilskuddBelop", faktiskilsagn.getTilskuddListe()
                .stream()
                .allMatch(tilskudd -> tilskudd.getTilskuddBelop().equals("142000")));


        assertNull("deltaker", faktiskilsagn.getDeltaker());

        assertEquals("antallDeltakere", "17", faktiskilsagn.getAntallDeltakere());
        assertEquals("antallTimeverk", "2932", faktiskilsagn.getAntallTimeverk());

        assertEquals("navEnhet.navKontor", "1187", faktiskilsagn.getNavEnhet().getNavKontor());
        assertEquals("navEnhet.navKontorNavn", "NAV Tiltak Rogaland", faktiskilsagn.getNavEnhet().getNavKontorNavn());
        assertEquals("navEnhet.postAdresse", "Postboks 420", faktiskilsagn.getNavEnhet().getPostAdresse());
        assertEquals("navEnhet.postNummer", "4002", faktiskilsagn.getNavEnhet().getPostNummer());
        assertEquals("navEnhet.postSted", "STAVANGER", faktiskilsagn.getNavEnhet().getPostSted());
        assertEquals("navEnhet.telefon", "55553333", faktiskilsagn.getNavEnhet().getTelefon());
        assertEquals("navEnhet.faks", "52048361", faktiskilsagn.getNavEnhet().getFaks());

        assertEquals("beslutter.fornavn", "Evy", faktiskilsagn.getBeslutter().getFornavn());
        assertEquals("beslutter.etternavn", "Strømmen", faktiskilsagn.getBeslutter().getEtternavn());

        assertEquals("saksbehandler.fornavn", "Odd Helge", faktiskilsagn.getSaksbehandler().getFornavn());
        assertEquals("saksbehandler.etternavn", "Johannessen", faktiskilsagn.getSaksbehandler().getEtternavn());

        assertNull("kommentar", faktiskilsagn.getKommentar());
    }
}
