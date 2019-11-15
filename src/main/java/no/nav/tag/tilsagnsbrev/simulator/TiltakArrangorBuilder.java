package no.nav.tag.tilsagnsbrev.simulator;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TiltakArrangor;

public class TiltakArrangorBuilder {
    private String arbgiverNavn;
    private String kontoNummer;
    private String landKode;
    private String maalform;
    private String orgNummer;
    private String orgNummerMorselskap;
    private String postAdresse;
    private String postNummer;
    private String postSted;

    public TiltakArrangorBuilder medArbgiverNavn(String arbgiverNavn) {
        this.arbgiverNavn = arbgiverNavn;
        return this;
    }

    public TiltakArrangorBuilder medKontoNummer(String kontoNummer) {
        this.kontoNummer = kontoNummer;
        return this;
    }

    public TiltakArrangorBuilder medLandKode(String landKode) {
        this.landKode = landKode;
        return this;
    }

    public TiltakArrangorBuilder medMaalform(String maalform) {
        this.maalform = maalform;
        return this;
    }

    public TiltakArrangorBuilder medOrgNummer(String orgNummer) {
        this.orgNummer = orgNummer;
        return this;
    }

    public TiltakArrangorBuilder medOrgNummerMorselskap(String orgNummerMorselskap) {
        this.orgNummerMorselskap = orgNummerMorselskap;
        return this;
    }

    public TiltakArrangorBuilder medPostAdresse(String postAdresse) {
        this.postAdresse = postAdresse;
        return this;
    }

    public TiltakArrangorBuilder medPostNummer(String postNummer) {
        this.postNummer = postNummer;
        return this;
    }

    public TiltakArrangorBuilder medPostSted(String postSted) {
        this.postSted = postSted;
        return this;
    }

    public TiltakArrangor createTiltakArrangor() {
        return new TiltakArrangor(arbgiverNavn, kontoNummer, landKode, maalform, orgNummer, orgNummerMorselskap, postAdresse, postNummer, postSted);
    }
}