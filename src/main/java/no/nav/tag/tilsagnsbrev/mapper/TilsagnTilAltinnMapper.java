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
import java.time.LocalDateTime;

@Component
public class TilsagnTilAltinnMapper {

    @Autowired
    private AltinnProperties altinnProperties;

    private static final String ATTACHMENT_NAME_PREFIX = "NAV-Tilsagnsbrev ";
    private static final String FIL_EXT = ".pdf";
    private static final String SYSTEM_USERCODE = "TAG_TILSAGN";


    private static final String SERVICE_CODE = "5278"; //TODO Sjekk bruken av dette.
    private static final String SERVICE_EDITION = "1"; //TODO Sjekk bruken av dette.
    private static final String LANGUAGE_CODE = "1044"; //TODO Sjekk bruken av dette.
    private static final String SENDER_REF = "test_ref"; //TODO Sjekk bruken av dette.
    private static final String MSG_SENDER = "NAV"; //TODO Sjekk bruken av dette.

    public InsertCorrespondenceBasicV2 tilAltinnMelding(Tilsagn tilsagn, byte[] pdf) {
        return new InsertCorrespondenceBasicV2()
                .withSystemUserName(altinnProperties.getSystemBruker())
                .withSystemPassword(altinnProperties.getSystemPassord())
                .withSystemUserCode(SYSTEM_USERCODE)
                .withExternalShipmentReference(extShipmentRef())
                .withCorrespondence(new InsertCorrespondenceV2()
                        .withServiceCode(SERVICE_CODE)
                        .withServiceEdition(SERVICE_EDITION)
                        .withVisibleDateTime(fromLocalDate(LocalDateTime.now())) //TODO Sjekk
                        .withAllowSystemDeleteDateTime(fromLocalDate(LocalDateTime.now().plusMonths(3))) //TODO Sjekk
                        .withDueDateTime(fromLocalDate(LocalDateTime.now().plusMonths(3)))   //TODO Sjekk
                        .withAllowForwarding(false)
                        .withMessageSender(MSG_SENDER)
                        .withReportee(tilsagn.getTiltakArrangor().getOrgNummer())
                        .withContent(new ExternalContentV2()
                                .withLanguageCode(LANGUAGE_CODE)
                                .withAttachments(new AttachmentsV2()
                                        .withBinaryAttachments(new BinaryAttachmentExternalBEV2List()
                                                .withBinaryAttachmentV2(new BinaryAttachmentV2()
                                                        .withSendersReference(SENDER_REF)
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

    private XMLGregorianCalendar fromLocalDate(LocalDateTime localDateTime) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(localDateTime.toString());
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e); //TODO
        }
    }

    private static final String EXT_REF = "ESR_NAV";    //TODO Sjekk bruken av dette.
    private String extShipmentRef(){
        return  EXT_REF + Double.valueOf(Math.random() * 1000000000);
    }
}
