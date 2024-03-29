package no.nav.tag.tilsagnsbrev.mapper;

import no.altinn.schemas.serviceengine.formsengine._2009._10.TransportType;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.AttachmentsV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.ExternalContentV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.InsertCorrespondenceV2;
import no.altinn.schemas.services.serviceengine.notification._2009._10.*;
import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.altinn.services.serviceengine.reporteeelementlist._2010._10.BinaryAttachmentExternalBEV2List;
import no.altinn.services.serviceengine.reporteeelementlist._2010._10.BinaryAttachmentV2;
import no.nav.tag.tilsagnsbrev.DatoUtils;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TiltakArrangor;
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
    private static final String VARSLING_EMNE_PREFIX = "Tilskuddsbrev for ";
    private static final String VARSLING_EMNE_SUFFIX = " er tilgjengelig i Altinn";
    private static final String VARSLING_TEKST_SUFFIX = " er tilgjengelig. Logg inn i Altinn for å se innholdet.";
    private static final String VARSLING_TEKST_FOOTER = "Vennlig hilsen NAV";
    private static final String START_AVSNITT = "<p>";
    private static final String SLUTT_AVSNITT = "</p>";

    public InsertCorrespondenceBasicV2 tilAltinnMelding(final Tilsagn tilsagn, final byte[] pdf) {
        return new InsertCorrespondenceBasicV2()
                .withSystemUserName(altinnProperties.getSystemBruker())
                .withSystemPassword(altinnProperties.getSystemPassord())
                .withSystemUserCode(SYSTEM_USERCODE)
                .withExternalShipmentReference(extShipmentRef())
                .withCorrespondence(new InsertCorrespondenceV2()
                        .withServiceCode(SERVICE_CODE)
                        .withServiceEdition(SERVICE_EDITION)
                        .withVisibleDateTime(fromLocalDate(DatoUtils.getNow()))
                        .withAllowSystemDeleteDateTime(fromLocalDate(DatoUtils.getNow().plusYears(10)))
                        .withDueDateTime(fromLocalDate(tilsagn.getRefusjonfristDato().atTime(LocalTime.MIDNIGHT).plusNanos(1)))
                        .withAllowForwarding(false)
                        .withMessageSender(MSG_SENDER)
                        .withReportee(tilsagn.getTiltakArrangor().getOrgNummer())
                        .withNotifications(new NotificationBEList().withNotification(notification(tilsagn.getTiltakArrangor())))
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

    private Notification notification(TiltakArrangor tiltakArrangor) {

        final String GEN_TEXT = new StringBuilder(4)
                .append(VARSLING_EMNE_PREFIX)
                .append(tiltakArrangor.getOrgNummer())
                .append(" ")
                .append(tiltakArrangor.getArbgiverNavn())
                .toString();

        final String VARSLING_TEKST = new StringBuilder(7)
                .append(START_AVSNITT)
                .append(GEN_TEXT)
                .append(VARSLING_TEKST_SUFFIX)
                .append(SLUTT_AVSNITT)
                .append(START_AVSNITT)
                .append(VARSLING_TEKST_FOOTER)
                .append(SLUTT_AVSNITT)
                .toString();

        return new Notification()
                .withLanguageCode(LANGUAGE_CODE)
                .withShipmentDateTime(fromLocalDate(DatoUtils.getNow()))
                .withReceiverEndPoints(new ReceiverEndPointBEList()
                        .withReceiverEndPoint(new ReceiverEndPoint()
                                .withTransportType(TransportType.EMAIL)))
                .withFromAddress(FRA_EPOST_ALTINN)
                .withNotificationType(VARSLING_TYPE)
                .withTextTokens(new TextTokenSubstitutionBEList()
                        .withTextToken(
                                new TextToken()
                                        .withTokenNum(0)
                                        .withTokenValue(new StringBuilder(3)
                                                .append(GEN_TEXT)
                                                .append(VARSLING_EMNE_SUFFIX).toString()),
                                new TextToken()
                                        .withTokenNum(1)
                                        .withTokenValue(VARSLING_TEKST)
                        ));
    }

    private String vedleggNavn(Tilsagn tilsagn) {
        StringBuilder sb = new StringBuilder()
                .append(ATTACHMENT_NAME_PREFIX)
                .append(" ")
                .append(tilsagn.getTiltakNavn())
                .append(" ");

        if (tilsagn.erGruppeTilsagn()) {
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

    private String extShipmentRef() {
        return EXT_REF + Double.valueOf(Math.random() * 1000000000);
    }
}
