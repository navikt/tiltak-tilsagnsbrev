package no.nav.tag.tilsagnsbrev.mapper;

import no.nav.tag.tilsagnsbrev.dto.altinn.*;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.konfigurasjon.AltinnKonfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Base64;

@Component
public class TilsagnTilAltinnDto {

    @Autowired
    private AltinnKonfig altinnKonfig;

    private static final String ATTACHMENT_NAME_PREFIX = "NAV-Tilsagnsbrev ";
    private static final String FIL_EXT = ".pdf";

    public InsertCorrespondenceBasicV2 tilAltinnMelding(Tilsagn tilsagn, byte[] pdf) {
        InsertCorrespondenceBasicV2 insertCorrespondenceBasicV2 = new InsertCorrespondenceBasicV2();
        insertCorrespondenceBasicV2.setSystemUserName(altinnKonfig.getUser());
        insertCorrespondenceBasicV2.setSystemPassword(altinnKonfig.getPassword());
        insertCorrespondenceBasicV2.setExternalShipmentReference(tilsagn.getTilsagnNummer().getLoepenrSak());
        insertCorrespondenceBasicV2.setCorrespondenceObject(opprettBody(tilsagn, pdf));
        return insertCorrespondenceBasicV2;
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
}
