package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.dto.altinn.header.BusinessScope;
import no.nav.tag.tilsagnsbrev.dto.altinn.header.BusinessService;
import no.nav.tag.tilsagnsbrev.dto.altinn.header.Scope;
import no.nav.tag.tilsagnsbrev.dto.altinn.header.StandardBusinessDocumentHeader;
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

        //TODO gjør ferdig
        return header;
    }
}
