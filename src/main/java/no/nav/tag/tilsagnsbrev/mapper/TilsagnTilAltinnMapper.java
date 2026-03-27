package no.nav.tag.tilsagnsbrev.mapper;

import no.altinn.schemas.serviceengine.formsengine._2009._10.TransportType;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.AttachmentsV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.ExternalContentV2;
import no.altinn.schemas.services.serviceengine.correspondence._2010._10.InsertCorrespondenceV2;
import no.altinn.schemas.services.serviceengine.notification._2009._10.Notification;
import no.altinn.schemas.services.serviceengine.notification._2009._10.NotificationBEList;
import no.altinn.schemas.services.serviceengine.notification._2009._10.ReceiverEndPoint;
import no.altinn.schemas.services.serviceengine.notification._2009._10.ReceiverEndPointBEList;
import no.altinn.schemas.services.serviceengine.notification._2009._10.TextToken;
import no.altinn.schemas.services.serviceengine.notification._2009._10.TextTokenSubstitutionBEList;
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

    private static final no.altinn.schemas.services.serviceengine.correspondence._2010._10.ObjectFactory corrFactory =
            new no.altinn.schemas.services.serviceengine.correspondence._2010._10.ObjectFactory();
    private static final no.altinn.schemas.services.serviceengine.notification._2009._10.ObjectFactory notifFactory =
            new no.altinn.schemas.services.serviceengine.notification._2009._10.ObjectFactory();
    private static final no.altinn.services.serviceengine.reporteeelementlist._2010._10.ObjectFactory attachFactory =
            new no.altinn.services.serviceengine.reporteeelementlist._2010._10.ObjectFactory();

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
        InsertCorrespondenceBasicV2 request = new InsertCorrespondenceBasicV2();
        request.setSystemUserName(altinnProperties.getSystemBruker());
        request.setSystemPassword(altinnProperties.getSystemPassord());
        request.setSystemUserCode(SYSTEM_USERCODE);
        request.setExternalShipmentReference(extShipmentRef());
        request.setCorrespondence(buildCorrespondence(tilsagn, pdf));
        return request;
    }

    private InsertCorrespondenceV2 buildCorrespondence(Tilsagn tilsagn, byte[] pdf) {
        InsertCorrespondenceV2 corr = new InsertCorrespondenceV2();
        corr.setServiceCode(corrFactory.createInsertCorrespondenceV2ServiceCode(SERVICE_CODE));
        corr.setServiceEdition(corrFactory.createInsertCorrespondenceV2ServiceEdition(SERVICE_EDITION));
        corr.setVisibleDateTime(fromLocalDate(DatoUtils.getNow()));
        corr.setAllowSystemDeleteDateTime(corrFactory.createInsertCorrespondenceV2AllowSystemDeleteDateTime(fromLocalDate(DatoUtils.getNow().plusYears(10))));
        corr.setDueDateTime(fromLocalDate(tilsagn.getRefusjonfristDato().atTime(LocalTime.MIDNIGHT).plusNanos(1)));
        corr.setAllowForwarding(corrFactory.createInsertCorrespondenceV2AllowForwarding(false));
        corr.setMessageSender(corrFactory.createInsertCorrespondenceV2MessageSender(MSG_SENDER));
        corr.setReportee(corrFactory.createInsertCorrespondenceV2Reportee(tilsagn.getTiltakArrangor().getOrgNummer()));
        corr.setNotifications(corrFactory.createInsertCorrespondenceV2Notifications(buildNotifications(tilsagn.getTiltakArrangor())));
        corr.setContent(corrFactory.createInsertCorrespondenceV2Content(buildContent(tilsagn, pdf)));
        return corr;
    }

    private NotificationBEList buildNotifications(TiltakArrangor tiltakArrangor) {
        NotificationBEList list = new NotificationBEList();
        list.getNotification().add(notification(tiltakArrangor));
        return list;
    }

    private ExternalContentV2 buildContent(Tilsagn tilsagn, byte[] pdf) {
        BinaryAttachmentV2 attachment = new BinaryAttachmentV2();
        attachment.setData(attachFactory.createBinaryAttachmentV2Data(pdf));
        attachment.setFileName(attachFactory.createBinaryAttachmentV2FileName(FILE_NAME_PREFIX + tilsagn.getTiltakNavn() + FIL_EXT));
        attachment.setName(attachFactory.createBinaryAttachmentV2Name(vedleggNavn(tilsagn)));

        BinaryAttachmentExternalBEV2List binaryList = new BinaryAttachmentExternalBEV2List();
        binaryList.getBinaryAttachmentV2().add(attachment);

        AttachmentsV2 attachments = new AttachmentsV2();
        attachments.setBinaryAttachments(corrFactory.createAttachmentsV2BinaryAttachments(binaryList));

        ExternalContentV2 content = new ExternalContentV2();
        content.setLanguageCode(corrFactory.createExternalContentV2LanguageCode(LANGUAGE_CODE));
        content.setMessageTitle(corrFactory.createExternalContentV2MessageTitle(vedleggNavn(tilsagn)));
        content.setAttachments(corrFactory.createExternalContentV2Attachments(attachments));
        return content;
    }

    private Notification notification(TiltakArrangor tiltakArrangor) {
        final String genText = VARSLING_EMNE_PREFIX + tiltakArrangor.getOrgNummer() + " " + tiltakArrangor.getArbgiverNavn();
        final String varslingTekst = START_AVSNITT + genText + VARSLING_TEKST_SUFFIX + SLUTT_AVSNITT
                + START_AVSNITT + VARSLING_TEKST_FOOTER + SLUTT_AVSNITT;

        ReceiverEndPoint endpoint = new ReceiverEndPoint();
        endpoint.setTransportType(notifFactory.createReceiverEndPointTransportType(TransportType.EMAIL));

        ReceiverEndPointBEList endpointList = new ReceiverEndPointBEList();
        endpointList.getReceiverEndPoint().add(endpoint);

        TextToken token0 = new TextToken();
        token0.setTokenNum(0);
        token0.setTokenValue(notifFactory.createTextTokenTokenValue(genText + VARSLING_EMNE_SUFFIX));

        TextToken token1 = new TextToken();
        token1.setTokenNum(1);
        token1.setTokenValue(notifFactory.createTextTokenTokenValue(varslingTekst));

        TextTokenSubstitutionBEList textTokens = new TextTokenSubstitutionBEList();
        textTokens.getTextToken().add(token0);
        textTokens.getTextToken().add(token1);

        Notification notif = new Notification();
        notif.setLanguageCode(notifFactory.createNotificationLanguageCode(LANGUAGE_CODE));
        notif.setShipmentDateTime(fromLocalDate(DatoUtils.getNow()));
        notif.setReceiverEndPoints(notifFactory.createNotificationReceiverEndPoints(endpointList));
        notif.setFromAddress(notifFactory.createNotificationFromAddress(FRA_EPOST_ALTINN));
        notif.setNotificationType(notifFactory.createNotificationNotificationType(VARSLING_TYPE));
        notif.setTextTokens(notifFactory.createNotificationTextTokens(textTokens));
        return notif;
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
