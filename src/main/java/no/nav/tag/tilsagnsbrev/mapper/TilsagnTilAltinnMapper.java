package no.nav.tag.tilsagnsbrev.mapper;

import no.altinn.schemas.services.serviceengine.correspondence._2010._10.AttachmentsV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.ExternalContentV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.InsertCorrespondenceV2;
import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.altinn.services.serviceengine.reporteeelementlist._2010._10.BinaryAttachmentExternalBEV2List;
import no.altinn.services.serviceengine.reporteeelementlist._2010._10.BinaryAttachmentV2;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.konfigurasjon.altinn.AltinnProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;

@Component
public class TilsagnTilAltinnMapper {

    @Autowired
    private AltinnProperties altinnProperties;

    private static final String ATTACHMENT_NAME_PREFIX = "NAV-Tilsagnsbrev ";
    private static final String FIL_EXT = ".pdf";

    public InsertCorrespondenceBasicV2 tilAltinnMelding(Tilsagn tilsagn, byte[] pdf) {
        return new InsertCorrespondenceBasicV2()
                .withSystemUserName(altinnProperties.getUser())
                .withSystemPassword(altinnProperties.getPassword())
                .withExternalShipmentReference(tilsagn.getTilsagnNummer().getLoepenrSak())
                .withCorrespondence(new InsertCorrespondenceV2()
                        .withVisibleDateTime(fromLocadate(LocalDate.now()))
                        .withReportee(tilsagn.getTiltakArrangor().getOrgNummer())
                        .withContent(new ExternalContentV2()
                                .withAttachments(new AttachmentsV2()
                                        .withBinaryAttachments(new BinaryAttachmentExternalBEV2List()
                                                .withBinaryAttachmentV2(new BinaryAttachmentV2()
                                                        .withData(pdf)
                                                        .withFileName(new StringBuilder()
                                                                .append(tilsagn.getTiltakArrangor().getOrgNummer())
                                                                .append(" ")
                                                                .append(tilsagn.getTilsagnDato())
                                                                .append(FIL_EXT)
                                                                .toString())
                                                        .withName(new StringBuilder()
                                                                .append(ATTACHMENT_NAME_PREFIX)
                                                                .append(tilsagn.getTiltakArrangor().getOrgNummer())
                                                                .append(" ")
                                                                .append(tilsagn.getTilsagnDato())
                                                                .toString()))))));
    }

    private XMLGregorianCalendar fromLocadate(LocalDate localDate) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(localDate.toString());
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace(); //TODO
        }
        return null;
    }
}
