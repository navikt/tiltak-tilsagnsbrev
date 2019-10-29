package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.dto.altinn.*;
import no.nav.tag.tilsagnsbrev.dto.altinn.header.*;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Base64;

@Component
public class TilsagnTilAltinnXml {

    public String tilAltinnMelding(Tilsagn tilsagn, byte[] pdf) {

        StandardBusinessDocumentHeader header = opprettHeader(tilsagn);
        InsertCorrespondenceV2 insertCorrespondenceV2 = new InsertCorrespondenceV2();
        insertCorrespondenceV2.setExternalShipmentReference("35313232343335472480806A76504F3D33323130");
        insertCorrespondenceV2.setSystemUserCode("NAV-TILSAGNSBREV");
        insertCorrespondenceV2.setCorrespondenceObject(opprettBody(tilsagn, pdf));
        return tilsagnsbrevTilXml(new StandardBusinessDocument(opprettHeader(tilsagn), insertCorrespondenceV2));
    }

    private StandardBusinessDocumentHeader opprettHeader(Tilsagn tilsagn) {
        StandardBusinessDocumentHeader header = new StandardBusinessDocumentHeader();

        Scope scope = new Scope();
        scope.setType("ConversationId"); //TODO
        scope.setInstanceIdentifier("unik"); //TODO hvordan generere
        scope.setBusinessService(new BusinessService("ALTINNRapportere", ""));  //TODO xzmlgen m/attr

        BusinessScope businessScope = new BusinessScope();
        businessScope.getScope().add(scope);
        header.setBusinessScopeObject(businessScope);

        header.setDocumentIdentificationObject(new DocumentIdentification(tilsagn.getTilsagnDato().toString(), "INstanceId"));
        header.setReceiverObject(new Receiver(tilsagn.getTiltakArrangor().getOrgNummer()));
        header.setSenderObject(new Sender(tilsagn.getNavEnhet().getNavKontor()));
        return header;
    }


    private Correspondence opprettBody(Tilsagn tilsagn, byte[] pdf) {
        Correspondence correspondence = new Correspondence();
        correspondence.setServiceCode("SCVKODE");
        correspondence.setServiceEdition("1");
        correspondence.setReportee("");
        correspondence.setContent(opprettContent(tilsagn, pdf));

        correspondence.setAllowSystemDeleteDateTimeObject("");
        return correspondence;
    }

    private Content opprettContent(Tilsagn tilsagn, byte[] pdf) {
        Content content = new Content();
        content.setLanguageCode("");
        content.setMessageTitle("Tilsagnsbrev");
        content.setMessageSummary("");
        content.setMessageBody("");//Bare noe skjemagreier

        BinaryAttachmentV2 binaryAttachmentV2 = new BinaryAttachmentV2();
        binaryAttachmentV2.setFunctionTyp("Unspecified");
        binaryAttachmentV2.setFileName(new StringBuilder().append(tilsagn.getTiltakArrangor().getOrgNummer()).append(" ").append(tilsagn.getTilsagnDato()).append(".pdf").toString());
        binaryAttachmentV2.setName("");
        binaryAttachmentV2.setEncrypted("false");
        binaryAttachmentV2.setData(Base64.getEncoder().encodeToString(pdf));

        content.getAttachments().getBinaryAttachments().getBinaryAttachmentV2().add(binaryAttachmentV2);
        return content;
    }

    private String tilsagnsbrevTilXml(StandardBusinessDocument standardBusinessDocument) {
        StringWriter stringWriter = new StringWriter();
        JAXBContext context;
        Marshaller marshaller;
        try {
            context = JAXBContext.newInstance(StandardBusinessDocument.class);
            marshaller = context.createMarshaller();
            marshaller.marshal(standardBusinessDocument, stringWriter);
        } catch (Exception e) {
            throw new RuntimeException("Feil ved oppretting av dokument xml: ", e);
        }
        return stringWriter.getBuffer().toString();
    }


}
