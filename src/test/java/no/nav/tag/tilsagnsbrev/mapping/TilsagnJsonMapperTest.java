package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.junit.Test;

import static no.nav.tag.tilsagnsbrev.Testdata.*;
import static org.junit.Assert.*;

public class TilsagnJsonMapperTest {

    TilsagnJsonMapper tilsagnJsonMapper = new TilsagnJsonMapper();

    @Test
    public void mapperGoldengateJsonTilTilsagn() {

        String requestJson = Testdata.hentJsonFil(JSON_FIL_FEILER);
        Tilsagn tilsagn = tilsagnJsonMapper.goldengateJsonTilTilsagn(requestJson);

        assertEquals("tilsagnNummer.aar", "2019", tilsagn.getTilsagnNummer().getAar());
        assertEquals("tilsagnNummer.loepenrSak", "366023", tilsagn.getTilsagnNummer().getLoepenrSak());
        assertEquals("tilsagnNummer.loepenrTilsagn", "1", tilsagn.getTilsagnNummer().getLoepenrTilsagn());

        assertEquals("tilsagnDato", TILSAGNSDATO, tilsagn.getTilsagnDato());
        assertEquals("tiltakKode", "BIO", tilsagn.getTiltakKode());
        assertEquals("tiltakNavn", "Bedriftsintern opplæring (BIO)", tilsagn.getTiltakNavn());
        assertEquals("administrasjonKode", "INST", tilsagn.getAdministrasjonKode());

        assertEquals("periode.fraDato", FRA_DATO, tilsagn.getPeriode().getFraDato());
        assertEquals("periode.tilDato", TIL_DATO, tilsagn.getPeriode().getTilDato());

        assertEquals("arbgiverNavn", "L.S. Solland AS", tilsagn.getTiltakArrangor().getArbgiverNavn());
        assertEquals("landKode", "NO", tilsagn.getTiltakArrangor().getLandKode());
        assertEquals("postAdresse", "Pedersgata 110", tilsagn.getTiltakArrangor().getPostAdresse());
        assertEquals("postNummer", "4014", tilsagn.getTiltakArrangor().getPostNummer());
        assertEquals("postSted", "STAVANGER", tilsagn.getTiltakArrangor().getPostSted());
        assertEquals("orgNummerMorselskap", "918160922", tilsagn.getTiltakArrangor().getOrgNummerMorselskap());
        assertEquals("orgNummer", "973152289", tilsagn.getTiltakArrangor().getOrgNummer());
        assertEquals("kontoNummer", "32010501481", tilsagn.getTiltakArrangor().getKontoNummer());
        assertEquals("maalform", "NO", tilsagn.getTiltakArrangor().getMaalform());

        assertEquals("totaltTilskuddbelop", "284000", tilsagn.getTotaltTilskuddbelop());
        assertEquals("valutaKode", "NOK", tilsagn.getValutaKode());

        assertTrue("tilskuddListe.TilskuddType", tilsagn.getTilskuddListe()
                .stream()
                .anyMatch(tilskudd -> tilskudd.getTilskuddType().equals("Opplæringstilskudd")));

        assertTrue("tilskuddListe.TilskuddType", tilsagn.getTilskuddListe()
                .stream()
                .anyMatch(tilskudd -> tilskudd.getTilskuddType().equals("Lønnstilskudd")));

        assertTrue("tilskuddListe.tilskuddBelop", tilsagn.getTilskuddListe()
                .stream()
                .allMatch(tilskudd -> tilskudd.getTilskuddBelop().equals("142000")));


        assertNull("deltaker", tilsagn.getDeltaker());

        assertEquals("antallDeltakere", "17", tilsagn.getAntallDeltakere());
        assertEquals("antallTimeverk", "2932", tilsagn.getAntallTimeverk());

        assertEquals("navEnhet.navKontor", "1187", tilsagn.getNavEnhet().getNavKontor());
        assertEquals("navEnhet.navKontorNavn", "NAV Tiltak Rogaland", tilsagn.getNavEnhet().getNavKontorNavn());
        assertEquals("navEnhet.postAdresse", "Postboks 420", tilsagn.getNavEnhet().getPostAdresse());
        assertEquals("navEnhet.postNummer", "4002", tilsagn.getNavEnhet().getPostNummer());
        assertEquals("navEnhet.postSted", "STAVANGER", tilsagn.getNavEnhet().getPostSted());
        assertEquals("navEnhet.telefon", "55553333", tilsagn.getNavEnhet().getTelefon());
        assertEquals("navEnhet.faks", "52048361", tilsagn.getNavEnhet().getFaks());

        assertEquals("beslutter.fornavn", "Evy", tilsagn.getBeslutter().getFornavn());
        assertEquals("beslutter.etternavn", "Strømmen", tilsagn.getBeslutter().getEtternavn());

        assertEquals("saksbehandler.fornavn", "Odd Helge", tilsagn.getSaksbehandler().getFornavn());
        assertEquals("saksbehandler.etternavn", "Johannessen", tilsagn.getSaksbehandler().getEtternavn());

        assertNull("kommentar", tilsagn.getKommentar());
    }
}
