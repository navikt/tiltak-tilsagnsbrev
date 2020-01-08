package no.nav.tag.tilsagnsbrev.dto.journalpost;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DokumentVariant {

    private static final String FILTYPE_PDF = "PDFA";
    private static final String VARIANFORMAT = "ARKIV";

    private final String filtype = FILTYPE_PDF;
    private final String variantformat = VARIANFORMAT;
    private final String fysiskDokument;
}
