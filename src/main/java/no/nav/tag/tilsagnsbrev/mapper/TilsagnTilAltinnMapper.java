package no.nav.tag.tilsagnsbrev.mapper;

import no.altinn.schemas.serviceengine.formsengine._2009._10.TransportType;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.AttachmentsV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.ExternalContentV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.InsertCorrespondenceV2;
import no.altinn.schemas.services.serviceengine.notification._2009._10.*;
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
import java.time.LocalTime;

@Component
public class TilsagnTilAltinnMapper {

    @Autowired
    private AltinnProperties altinnProperties;

    private static final String ATTACHMENT_NAME_PREFIX = "Tilskuddsbrev ";
    private static final String FILE_NAME_PREFIX = "Tilskuddsbrev-";
    private static final String FIL_EXT = ".pdf";
    private static final String SYSTEM_USERCODE = "TAG_TILSAGN";
    private static final String EXT_REF = "ESR_NAV";
    private static final String SERVICE_CODE = "5278";
    private static final String SERVICE_EDITION = "1";
    private static final String LANGUAGE_CODE = "1044";
    private static final String MSG_SENDER = "NAV";
    private static final String FRA_EPOST_ALTINN = "noreply@altinn.no";
    private static final String VARSLING_TYPE = "TokenTextOnly";
    private static final String VARSLING_PREFIX = "Nytt tilskuddsbrev er sendt. Tiltak: ";

    public InsertCorrespondenceBasicV2 tilAltinnMelding(final Tilsagn tilsagn, final byte[] pdf) {
        return new InsertCorrespondenceBasicV2()
                .withSystemUserName(altinnProperties.getSystemBruker())
                .withSystemPassword(altinnProperties.getSystemPassord())
                .withSystemUserCode(SYSTEM_USERCODE)
                .withExternalShipmentReference(extShipmentRef())
                .withCorrespondence(new InsertCorrespondenceV2()
                        .withServiceCode(SERVICE_CODE)
                        .withServiceEdition(SERVICE_EDITION)
                        .withVisibleDateTime(fromLocalDate(LocalDateTime.now()))
                        .withAllowSystemDeleteDateTime(fromLocalDate(LocalDateTime.now().plusYears(10))) //TODO Sjekk
                        .withDueDateTime(fromLocalDate(tilsagn.getRefusjonfristDato().atTime(LocalTime.MIDNIGHT).plusNanos(1)))
                        .withAllowForwarding(false)
                        .withMessageSender(MSG_SENDER)
                        .withReportee(tilsagn.getTiltakArrangor().getOrgNummer())
                        .withNotifications(new NotificationBEList().withNotification(notification(tilsagn.getTiltakNavn())))
                        .withContent(new ExternalContentV2()
                                .withLanguageCode(LANGUAGE_CODE)
                                .withMessageTitle(vedleggNavn(tilsagn))
                                .withAttachments(new AttachmentsV2()
                                        .withBinaryAttachments(new BinaryAttachmentExternalBEV2List()
                                                .withBinaryAttachmentV2(new BinaryAttachmentV2()
                                                        .withData(pdf)
                                                        .withFileName(new StringBuilder()
                                                                .append(FILE_NAME_PREFIX)
                                                                .append(tilsagn.getTiltakNavn())
                                                                .append(FIL_EXT)
                                                                .toString())
                                                        .withName(vedleggNavn(tilsagn)))))));
    }

    private Notification notification(String tiltak) {
        return new Notification()
                .withLanguageCode(LANGUAGE_CODE)
                .withShipmentDateTime(fromLocalDate(LocalDateTime.now()))
                .withReceiverEndPoints(new ReceiverEndPointBEList()
                        .withReceiverEndPoint(new ReceiverEndPoint()
                                .withTransportType(TransportType.EMAIL)))

                .withFromAddress(FRA_EPOST_ALTINN)
                .withNotificationType(VARSLING_TYPE)
                .withTextTokens(new TextTokenSubstitutionBEList()
                        .withTextToken(new TextToken().withTokenNum(0).withTokenValue(VARSLING_PREFIX + tiltak)));
//
//
//                        ));
    }

    private String vedleggNavn(Tilsagn tilsagn){
        StringBuilder sb = new StringBuilder()
                .append(ATTACHMENT_NAME_PREFIX)
                .append(" ")
                .append(tilsagn.getTiltakNavn())
                .append(" ");

        if (tilsagn.erGruppeTilsagn()){
            return sb.append(tilsagn.getPeriode().getFraDato()).append(" til ").append(tilsagn.getPeriode().getTilDato()).toString();
        }
        return sb.append(tilsagn.getDeltaker().getEtternavn()).toString();
    }

    private XMLGregorianCalendar fromLocalDate(LocalDateTime localDateTime) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(localDateTime.toString());
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private String extShipmentRef(){
        return  EXT_REF + Double.valueOf(Math.random() * 1000000000);
    }
}
