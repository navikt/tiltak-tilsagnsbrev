package no.nav.tag.tilsagnsbrev.dto.altinn.header;

import lombok.Data;

@Data
public class Scope {
    String Type; //ConversationId, Action
    String InstanceIdentifier; // LIk DocumentIdentification.InstanceIdentifier, Trekkoppgjorsrapport-T12
    BusinessService businessService;
}

