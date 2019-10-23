package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

public class NavEnhetBuilder {
    private String faks;
    private String navKontor;
    private String navKontorNavn;
    private String postAdresse;
    private String postNummer;
    private String postSted;
    private String telefon;

    public NavEnhetBuilder medFaks(String faks) {
        this.faks = faks;
        return this;
    }

    public NavEnhetBuilder medNavKontor(String navKontor) {
        this.navKontor = navKontor;
        return this;
    }

    public NavEnhetBuilder medNavKontorNavn(String navKontorNavn) {
        this.navKontorNavn = navKontorNavn;
        return this;
    }

    public NavEnhetBuilder medPostAdresse(String postAdresse) {
        this.postAdresse = postAdresse;
        return this;
    }

    public NavEnhetBuilder medPostNummer(String postNummer) {
        this.postNummer = postNummer;
        return this;
    }

    public NavEnhetBuilder medPostSted(String postSted) {
        this.postSted = postSted;
        return this;
    }

    public NavEnhetBuilder medTelefon(String telefon) {
        this.telefon = telefon;
        return this;
    }

    public NavEnhet createNavEnhet() {
        return new NavEnhet(faks, navKontor, navKontorNavn, postAdresse, postNummer, postSted, telefon);
    }
}