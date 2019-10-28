package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;
import no.nav.tag.tilsagnsbrev.dto.altinn.header.StandardBusinessDocumentHeader;

@Data
public class StandardBusinessDocument {
    StandardBusinessDocumentHeader StandardBusinessDocumentHeaderObject;
        InsertCorrespondenceV2 InsertCorrespondenceV2Object;



}
