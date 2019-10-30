package no.nav.tag.tilsagnsbrev.dto.altinn.header;

import lombok.Data;

@Data
public class Scope {

    private final static String ELEM_TYPE = "ConversationId";

    private final String Type = ELEM_TYPE;
    String InstanceIdentifier; // LIk DocumentIdentification.InstanceIdentifier, Trekkoppgjorsrapport-T12
    BusinessService businessService;
}

