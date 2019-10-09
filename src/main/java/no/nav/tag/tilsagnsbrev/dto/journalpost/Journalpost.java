package no.nav.tag.tilsagnsbrev.dto.journalpost;

import lombok.Data;

import java.util.List;

@Data
public class Journalpost {
    private final static String JOURNALPOST_TYPE = "UTGAAENDE";
     private final static String BEHANDLINGSTEMA = "";
    private final static String KANAL = "ALTINN";
    private final static String TEMA = "TIL";

    final static String TITTEL_MIDLERTIDIG = "Tilsagn om midlertidig lønnstilskudd";
    public final static String EKSTREF_PREFIKS = "AVT";

    private final String journalposttype = JOURNALPOST_TYPE;
    private final String behandlingsTema = BEHANDLINGSTEMA;
    private final String kanal = KANAL;
    private final String tema = TEMA;
    private String eksternReferanseId;

    private Bruker bruker;
    private final String tittel = TITTEL_MIDLERTIDIG;
    private List<Dokument> dokumenter;

    private boolean midlertidigLonnstilskudd;
}