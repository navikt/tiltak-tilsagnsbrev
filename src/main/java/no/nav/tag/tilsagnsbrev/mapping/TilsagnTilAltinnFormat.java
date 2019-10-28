package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.dto.altinn.header.*;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;

public class TilsagnTilAltinnFormat {

    public String tilAltinnMelding(Tilsagn tilsagn, byte[] pdf){

        StandardBusinessDocumentHeader header = lagHeader(tilsagn);

        return "";
    }

    private StandardBusinessDocumentHeader lagHeader(Tilsagn tilsagn) {
        StandardBusinessDocumentHeader header = new StandardBusinessDocumentHeader();

        Scope scope = new Scope();
        scope.setType("ConversationId"); //TODO
        scope.setInstanceIdentifier("unik"); //TODO hvordan generere
        scope.setBusinessService(new BusinessService("ALTINNRapportere",""));  //TODO xzmlgen m/attr

        BusinessScope businessScope = new BusinessScope();
        businessScope.getScope().add(scope);
        header.setBusinessScopeObject(businessScope);

        header.setDocumentIdentificationObject(new DocumentIdentification(tilsagn.getTilsagnDato().toString(), "INstanceId"));

        header.setReceiverObject(new Receiver(tilsagn.getTiltakArrangor().getOrgNummer()));
        header.setSenderObject(new Sender(tilsagn.getNavEnhet().getNavKontor()));

        return header;
    }
}
