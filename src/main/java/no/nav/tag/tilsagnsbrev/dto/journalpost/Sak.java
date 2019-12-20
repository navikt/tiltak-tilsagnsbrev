package no.nav.tag.tilsagnsbrev.dto.journalpost;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sak {
    private static final String FAGSAK_SYSTEM = "AO01";
    private static final String TYPE_SAK = "FAGSAK";

    private final String fagsaksystem = FAGSAK_SYSTEM;
    private final String sakstype = TYPE_SAK;

    private final String fagsakId;
}
