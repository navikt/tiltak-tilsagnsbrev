package no.nav.tag.tilsagnsbrev.dto.altinn.header;

import lombok.Data;

@Data
public class StandardBusinessDocumentHeader {

    final private String HeaderVersion = "1.0";
    Sender SenderObject;
    Receiver ReceiverObject;
    DocumentIdentification DocumentIdentificationObject;
    BusinessScope BusinessScopeObject;
}
