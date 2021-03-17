package no.nav.tag.tilsagnsbrev.mapper;

import lombok.Getter;

@Getter
public enum TiltakType {
    ARBFORB("ARBFORB", "Tilsagnsbrev Arbeidsforberedende trening", "arena-tilskudd-arbforb-tren"),
    EKSPEBIST("EKSPEBIST", "Tilsagnsbrev Ekspertbistand", "arena-tilskudd-ekspebist"),
    FUNKSJASS("FUNKSJASS", "Tilsagnsbrev Funksjonsassistanse", "arena-tilskudd-funksjass"),
    INKLUTILS("INKLUTILS", "Tilsagnsbrev Inkluderingstilskudd", "arena-tilskudd-inklutils"),
    MENTOR("MENTOR", "Tilsagnsbrev Mentor", "arena-tilskudd-mentor"),
    MIDLONTIL("MIDLONTIL", "Tilsagnsbrev Midlertidig lønnstilskudd", "arena-tilskudd-midl-lønnst"),
    VARLONTIL("VARLONTIL", "Tilsagnsbrev Varig lønnstilskudd", "arena-tilskudd-var-lønnst"),
    ENKELAMO("ENKELAMO", "Tilsagnsbrev Opplæring AMO", "arena-tilskudd-oppl-amo"),
    ENKFAGYRKE("ENKFAGYRKE", "Tilsagnsbrev Enkeltplass fag og yrkesopplæring", "arena-tilskudd-enk-fagyrk"),
    GRUFAGYRKE("GRUFAGYRKE", "Tilsagnsbrev Gruppe fag og yrkesopplæring", "arena-tilskudd-grp-fagyrk"),
    HOYEREUTD("HOYEREUTD", "Tilsagnsbrev Opplæring høyere utdanning", "arena-tilskudd-oppl-høyutd"),
    VASV("VASV", "Tilsagnsbrev Varig tilrettelagt arbeid", "arena-tilskudd-vartil-arb"),
    VATIAROR("VATIAROR", "Tilsagnsbrev Varig tilrettelagt arbeid i ordinær virksomhet", "arena-tilskudd-vartil_ordvirk"),
    FORSAMOENK("FORSAMOENK", "Forsøk AMO enkeltplass", "arena-tilskudd-forsok-enk-amo"),
    FORSFAGGRU("FORSFAGGRU", "Forsøk fag- og yrkesopplæring gruppe", "arena-tilskudd-forsok-grp-fagyrk"),
    FORSFAGENK("FORSFAGENK", "Forsøk fag- og yrkesopplæring enkeltplass", "arena-tilskudd-enk-fagyrk");


    private final String tiltakskode;
    private final String tittel;
    private final String brevkode;

    TiltakType(String tiltakskode, String tittel, String brevkode) {
        this.tiltakskode = tiltakskode;
        this.tittel = tittel;
        this.brevkode = brevkode;
    }
}