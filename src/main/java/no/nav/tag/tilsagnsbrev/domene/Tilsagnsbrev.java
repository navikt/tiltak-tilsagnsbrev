package no.nav.tag.tilsagnsbrev.domene;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Tilsagnsbrev {

    private String navKontor;
    private String navReferanse;
    private String navPostadresse;
    private String navBesoksadresse;
    private String navTlf;
    private String navFaks;
    private String navnSaksbehandler;

    private String oprettetDato;
    private String fraDato;
    private String tilDato;
    private String fristRefusjonskrav;
    private String tilskudd;
    private String tilskuddLonn;
    private String tilskuddTotalt;

    private String bedriftsnavn;
    private String orgNummer;
    private String bedriftsNummer;
    private String kontoNummer;

    private String deltakerNavn;
    private String deltakerFnr;
}
