package no.nav.tag.tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.domene.Tilsagnsbrev;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Testdata {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YYYY");

    public static Tilsagnsbrev tilsagnsbrev(){
        Tilsagnsbrev tilsagnsbrev = new Tilsagnsbrev();
        tilsagnsbrev.setBedriftsnavn("Hennings Varer");
        tilsagnsbrev.setBedriftsNummer("999998888");
        tilsagnsbrev.setOrgNummer("888889999");
        tilsagnsbrev.setKontoNummer("1111.22.33333");
        tilsagnsbrev.setTilskuddLonn("30.000");
        tilsagnsbrev.setTilskuddTotalt("60.000");

        tilsagnsbrev.setDeltakerNavn("Bjarte Tynning");
        tilsagnsbrev.setDeltakerFnr("11110012345");

        tilsagnsbrev.setOprettetDato(LocalDate.now().format(DATE_TIME_FORMATTER));
        tilsagnsbrev.setFraDato(LocalDate.now().plusMonths(1).format(DATE_TIME_FORMATTER));
        tilsagnsbrev.setTilDato(LocalDate.now().plusMonths(4).format(DATE_TIME_FORMATTER));
        tilsagnsbrev.setFristRefusjonskrav(LocalDate.now().plusWeeks(2).format(DATE_TIME_FORMATTER));

        tilsagnsbrev.setNavnSaksbehandler("Hennings Bror");
        tilsagnsbrev.setNavBesoksadresse("Waldemar Thranes gate 98");
        tilsagnsbrev.setNavFaks("11112222");
        tilsagnsbrev.setNavKontor("Løkka");
        tilsagnsbrev.setNavPostadresse("Pb 311, Løkka");
        tilsagnsbrev.setNavReferanse("Ref. 99");
        tilsagnsbrev.setNavTlf("99887766");
        return tilsagnsbrev;
    }
}
