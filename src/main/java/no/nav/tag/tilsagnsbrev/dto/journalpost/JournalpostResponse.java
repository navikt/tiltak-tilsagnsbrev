package no.nav.tag.tilsagnsbrev.dto.journalpost;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JournalpostResponse {

    static final String ID_TYPE_ORGNR = "ORGNR";

    private String journalpostId;
    private String journalstatus;
    private String melding;
}