package no.nav.tag.tilsagnsbrev.domene.journalpost;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Bruker {
    private final String idType = "ORGNR";
    private String id;
}
