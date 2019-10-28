package no.nav.tag.tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.*;

import java.time.LocalDate;
import java.util.List;

public class TilsagnBuilder {
    private String administrasjonKode;
    private String antallDeltakere;
    private String antallTimeverk;
    private Person beslutter;
    private Deltaker deltaker;
    private String kommentar;
    private NavEnhet navEnhet;
    private Periode periode;
    private Person saksbehandler;
    private LocalDate tilsagnDato;
    private TilsagnNummer tilsagnNummer;
    private List<Tilskudd> tilskuddListe;
    private TiltakArrangor tiltakArrangor;
    private String tiltakKode;
    private String tiltakNavn;
    private String totaltTilskuddbelop;
    private String valutaKode;

    public TilsagnBuilder medAdministrasjonKode(String administrasjonKode) {
        this.administrasjonKode = administrasjonKode;
        return this;
    }

    public TilsagnBuilder medAntallDeltakere(String antallDeltakere) {
        this.antallDeltakere = antallDeltakere;
        return this;
    }

    public TilsagnBuilder medAntallTimeverk(String antallTimeverk) {
        this.antallTimeverk = antallTimeverk;
        return this;
    }

    public TilsagnBuilder medBeslutter(Person beslutter) {
        this.beslutter = beslutter;
        return this;
    }

    public TilsagnBuilder medDeltaker(Deltaker deltaker) {
        this.deltaker = deltaker;
        return this;
    }

    public TilsagnBuilder medKommentar(String kommentar) {
        this.kommentar = kommentar;
        return this;
    }

    public TilsagnBuilder medNavEnhet(NavEnhet navEnhet) {
        this.navEnhet = navEnhet;
        return this;
    }

    public TilsagnBuilder medPeriode(Periode periode) {
        this.periode = periode;
        return this;
    }

    public TilsagnBuilder medSaksbehandler(Person saksbehandler) {
        this.saksbehandler = saksbehandler;
        return this;
    }

    public TilsagnBuilder medTilsagnDato(LocalDate tilsagnDato) {
        this.tilsagnDato = tilsagnDato;
        return this;
    }

    public TilsagnBuilder medTilsagnNummer(TilsagnNummer tilsagnNummer) {
        this.tilsagnNummer = tilsagnNummer;
        return this;
    }

    public TilsagnBuilder medTilskuddListe(List<Tilskudd> tilskuddListe) {
        this.tilskuddListe = tilskuddListe;
        return this;
    }

    public TilsagnBuilder medTiltakArrangor(TiltakArrangor tiltakArrangor) {
        this.tiltakArrangor = tiltakArrangor;
        return this;
    }

    public TilsagnBuilder medTiltakKode(String tiltakKode) {
        this.tiltakKode = tiltakKode;
        return this;
    }

    public TilsagnBuilder medTiltakNavn(String tiltakNavn) {
        this.tiltakNavn = tiltakNavn;
        return this;
    }

    public TilsagnBuilder medTotaltTilskuddbelop(String totaltTilskuddbelop) {
        this.totaltTilskuddbelop = totaltTilskuddbelop;
        return this;
    }

    public TilsagnBuilder medValutaKode(String valutaKode) {
        this.valutaKode = valutaKode;
        return this;
    }

    public Tilsagn createTilsagn() {
        return new Tilsagn(administrasjonKode, antallDeltakere, antallTimeverk, beslutter, deltaker, kommentar, navEnhet, periode, saksbehandler, tilsagnDato, tilsagnNummer, tilskuddListe, tiltakArrangor, tiltakKode, tiltakNavn, totaltTilskuddbelop, valutaKode);
    }
}