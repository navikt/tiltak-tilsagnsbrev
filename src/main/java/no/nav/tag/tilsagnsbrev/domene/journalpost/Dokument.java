package no.nav.tag.tilsagnsbrev.domene.journalpost;

import lombok.Data;

import java.util.List;

import static no.nav.tag.tilsagnsbrev.domene.journalpost.Journalpost.TITTEL_MIDLERTIDIG;

@Data
public class Dokument {
    private final String tittel = TITTEL_MIDLERTIDIG;
    private List<DokumentVariant> dokumentVarianter;
}
