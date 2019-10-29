package no.nav.tag.tilsagnsbrev.dto.altinn.header;

import lombok.Data;

@Data
public class StandardBusinessDocumentHeader {

    private static final String ELEM_HEADERVERSION = "1.0";

    final private String HeaderVersion = ELEM_HEADERVERSION;
    Partner SenderObject;
    Partner ReceiverObject;
    DocumentIdentification DocumentIdentificationObject;
    BusinessScope BusinessScopeObject;
}
