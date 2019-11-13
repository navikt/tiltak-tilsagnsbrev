package no.nav.tag.tilsagnsbrev.mapper;

import no.altinn.schemas.services.serviceengine.correspondence._2010._10.AttachmentsV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.ExternalContentV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.InsertCorrespondenceV2;
import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.altinn.services.serviceengine.reporteeelementlist._2010._10.BinaryAttachmentExternalBEV2List;
import no.altinn.services.serviceengine.reporteeelementlist._2010._10.BinaryAttachmentV2;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.konfigurasjon.AltinnKonfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        insertCorrespondenceBasicV2.setCorrespondence(opprettBody(tilsagn, pdf));;
        return insertCorrespondenceBasicV2;
    }

    private InsertCorrespondenceV2 opprettBody(Tilsagn tilsagn, byte[] pdf) {
        InsertCorrespondenceV2 insertCorrespondenceV2 = new InsertCorrespondenceV2();
        insertCorrespondenceV2.setReportee(tilsagn.getTiltakArrangor().getOrgNummer());
        insertCorrespondenceV2.setContent(opprettContent(tilsagn, pdf));
        return insertCorrespondenceV2;
    }

    private ExternalContentV2 opprettContent(Tilsagn tilsagn, byte[] pdf) {
        BinaryAttachmentV2 binaryAttachmentV2 = new BinaryAttachmentV2();
        binaryAttachmentV2.setFileName(new StringBuilder().append(tilsagn.getTiltakArrangor().getOrgNummer()).append(" ").append(tilsagn.getTilsagnDato()).append(FIL_EXT).toString());
        binaryAttachmentV2.setName(new StringBuilder().append(ATTACHMENT_NAME_PREFIX).append(tilsagn.getTiltakArrangor().getOrgNummer()).append(" ").append(tilsagn.getTilsagnDato()).toString());
        binaryAttachmentV2.setData(pdf);

        ExternalContentV2 externalContentV2 = new ExternalContentV2();
        AttachmentsV2 attachmentsV2 = new AttachmentsV2();
        attachmentsV2.setBinaryAttachments(new BinaryAttachmentExternalBEV2List());

        externalContentV2.setAttachments(attachmentsV2);
        externalContentV2.getAttachments().getBinaryAttachments().getBinaryAttachmentV2().add(binaryAttachmentV2);
        return externalContentV2;
    }
}
