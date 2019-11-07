package no.nav.tag.tilsagnsbrev.mapper;

import no.nav.tag.tilsagnsbrev.dto.altinn.*;
import no.nav.tag.tilsagnsbrev.dto.altinn.header.DocumentIdentification;
import no.nav.tag.tilsagnsbrev.dto.altinn.header.Partner;
import no.nav.tag.tilsagnsbrev.dto.altinn.header.StandardBusinessDocumentHeader;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Base64;

@Component
public class TilsagnXmlMapper {

    private static final String ATTACHMENT_NAME_PREFIX = "NAV - Tilsagnsbrev ";
    private static final String FIL_EXT = ".pdf";

    public String tilAltinnMelding(Tilsagn tilsagn, byte[] pdf) {
        InsertCorrespondenceV2 insertCorrespondenceV2 = new InsertCorrespondenceV2();
        insertCorrespondenceV2.setExternalShipmentReference(tilsagn.getTilsagnNummer().getLoepenrSak());
        insertCorrespondenceV2.setCorrespondenceObject(opprettBody(tilsagn, pdf));
        return tilsagnsbrevTilXml(new StandardBusinessDocument(opprettHeader(tilsagn), insertCorrespondenceV2));
    }

    private StandardBusinessDocumentHeader opprettHeader(Tilsagn tilsagn) {
        StandardBusinessDocumentHeader header = new StandardBusinessDocumentHeader();
        header.setDocumentIdentificationObject(new DocumentIdentification(tilsagn.getTilsagnDato().toString(), tilsagn.getTilsagnNummer().getLoepenrSak()));
        header.setReceiverObject(new Partner(tilsagn.getTiltakArrangor().getOrgNummer()));
        header.setSenderObject(new Partner(tilsagn.getNavEnhet().getNavKontor()));
        return header;
    }

    private Correspondence opprettBody(Tilsagn tilsagn, byte[] pdf) {
        Correspondence correspondence = new Correspondence();
        correspondence.setReportee(tilsagn.getTiltakArrangor().getOrgNummer());
        correspondence.setContent(opprettContent(tilsagn, pdf));
        return correspondence;
    }

    private Content opprettContent(Tilsagn tilsagn, byte[] pdf) {
        BinaryAttachmentV2 binaryAttachmentV2 = new BinaryAttachmentV2();
        binaryAttachmentV2.setFileName(new StringBuilder().append(tilsagn.getTiltakArrangor().getOrgNummer()).append(" ").append(tilsagn.getTilsagnDato()).append(FIL_EXT).toString());
        binaryAttachmentV2.setName(new StringBuilder().append(ATTACHMENT_NAME_PREFIX).append(tilsagn.getTiltakArrangor().getOrgNummer()).append(" ").append(tilsagn.getTilsagnDato()).toString());
        binaryAttachmentV2.setData(Base64.getEncoder().encodeToString(pdf));

        Content content = new Content();
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
