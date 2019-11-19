package no.nav.tag.tilsagnsbrev.dto.journalpost;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import static no.nav.tag.tilsagnsbrev.dto.journalpost.JournalpostResponse.ID_TYPE_ORGNR;

@Data
@RequiredArgsConstructor
public class Mottaker {

    private final String idType = ID_TYPE_ORGNR;
    private final String id;
    private final String navn;
}
