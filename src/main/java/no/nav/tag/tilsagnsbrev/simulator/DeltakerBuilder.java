package no.nav.tag.tilsagnsbrev.simulator;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Deltaker;

public class DeltakerBuilder {
    private String etternavn;
    private String fornavn;
    private String fodselsnr;
    private String landKode;
    private String postAdresse;
    private String postNummer;
    private String postSted;

    public DeltakerBuilder medEtternavn(String etternavn) {
        this.etternavn = etternavn;
        return this;
    }

    public DeltakerBuilder medFornavn(String fornavn) {
        this.fornavn = fornavn;
        return this;
    }

    public DeltakerBuilder medFodselsnr(String fodselsnr) {
        this.fodselsnr = fodselsnr;
        return this;
    }

    public DeltakerBuilder medLandKode(String landKode) {
        this.landKode = landKode;
        return this;
    }

    public DeltakerBuilder medPostAdresse(String postAdresse) {
        this.postAdresse = postAdresse;
        return this;
    }

    public DeltakerBuilder medPostNummer(String postNummer) {
        this.postNummer = postNummer;
        return this;
    }

    public DeltakerBuilder medPostSted(String postSted) {
        this.postSted = postSted;
        return this;
    }

    public Deltaker createDeltaker() {
        return new Deltaker(etternavn, fornavn, fodselsnr, landKode, postAdresse, postNummer, postSted);
    }
}