package no.nav.tag.tilsagnsbrev.dto.journalpost;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Journalpost {

    private final static String JOURNALPOST_TYPE = "UTGAAENDE";
    private final static String KANAL = "ALTINN";
    private final static String TEMA = "TIL";
    private final static String JOURNALFOERENDE_ENHET = "9999";

    public final static String TITTEL = "Tilsagnsbrev";

    private final String journalposttype = JOURNALPOST_TYPE;
    private final String kanal = KANAL;
    private final String tema = TEMA;
    private final String journalfoerendeEnhet = JOURNALFOERENDE_ENHET;

    private final String tittel;
    private final Bruker bruker;
    private final Mottaker avsenderMottaker;
    private final Sak sak;
    private final List<Dokument> dokumenter;
    private final String eksternReferanseId;
}