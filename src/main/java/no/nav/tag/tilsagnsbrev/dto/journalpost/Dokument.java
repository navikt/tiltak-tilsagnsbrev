package no.nav.tag.tilsagnsbrev.dto.journalpost;

import lombok.Data;

import java.util.List;

@Data
public class Dokument {
    private final String tittel;
    private final List<DokumentVariant> dokumentVarianter;
}
