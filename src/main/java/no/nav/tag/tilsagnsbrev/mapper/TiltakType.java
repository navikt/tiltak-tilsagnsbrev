package no.nav.tag.tilsagnsbrev.mapper;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TiltakType {
    ARBFORB("Tilsagnsbrev Arbeidsforberedende trening", "arbeidsforberedende-trening", "arena-tilskudd-arbforb-tren", false),
    EKSPEBIST("Tilsagnsbrev Ekspertbistand", "ekspertbistand", "arena-tilskudd-ekspebist", false),
    FUNKSJASS("Tilsagnsbrev Funksjonsassistanse", "funksjonsassistanse", "arena-tilskudd-funksjass", true),
    INKLUTILS("Tilsagnsbrev Inkluderingstilskudd", "inkluderingstilskudd", "arena-tilskudd-inklutils", true),
    MENTOR("Tilsagnsbrev Mentor", "mentortilskudd", "arena-tilskudd-mentor", true),
    MIDLONTIL("Tilsagnsbrev Midlertidig lønnstilskudd", "midlertidig-lonnstilskudd", "arena-tilskudd-midl-lønnst", false),
    VARLONTIL("Tilsagnsbrev Varig lønnstilskudd", "varig-lonnstilskudd", "arena-tilskudd-var-lønnst", false),
    ENKELAMO("Tilsagnsbrev Opplæring AMO", "opplaering-amo", "arena-tilskudd-oppl-amo", true),
    ENKFAGYRKE("Tilsagnsbrev Enkeltplass fag og yrkesopplæring", "opplaering-fagyrke-deltaker", "arena-tilskudd-enk-fagyrk", true),
    GRUFAGYRKE("Tilsagnsbrev Gruppe fag og yrkesopplæring", "opplaering-fagyrke-grp", "arena-tilskudd-grp-fagyrk", true),
    HOYEREUTD("Tilsagnsbrev Opplæring høyere utdanning", "opplaering-hoyere-utd", "arena-tilskudd-oppl-høyutd", false),
    VASV("Tilsagnsbrev Varig tilrettelagt arbeid", "varig-tilrettelagt-arbeid", "arena-tilskudd-vartil-arb", true),
    VATIAROR("Tilsagnsbrev Varig tilrettelagt arbeid i ordinær virksomhet", "varig-tilrettelagt-ordinar", "arena-tilskudd-vartil_ordvirk", true),
    FORSAMOENK("Tilsagnsbrev Forsøk AMO enkeltplass", "kompetansetiltak", "arena-tilskudd-forsok-enk-amo", false),
    FORSFAGGRU("Tilsagnsbrev Forsøk fag- og yrkesopplæring gruppe", "kompetansetiltak-grp", "arena-tilskudd-forsok-grp-fagyrk", false),
    FORSFAGENK("Tilsagnsbrev Forsøk fag- og yrkesopplæring enkeltplass", "kompetansetiltak", "arena-tilskudd-enk-fagyrk", true);

    private final String tittel;
    private final String pdfgenMal;
    private final String brevkode;
    private final boolean skalBehandles;

    TiltakType(String tittel, String pdfgenMal, String brevkode, boolean skalBehandles) {
        this.tittel = tittel;
        this.pdfgenMal = pdfgenMal;
        this.brevkode = brevkode;
        this.skalBehandles = skalBehandles;
    }

    public static TiltakType parse(String str) {
        return Arrays.stream(TiltakType.values())
            .filter(tiltakType -> tiltakType.name().equals(str))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Kan ikke parse tiltakstype: " + str));
    }
}
