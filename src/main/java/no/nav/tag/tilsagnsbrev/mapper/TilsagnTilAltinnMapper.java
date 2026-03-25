package no.nav.tag.tilsagnsbrev.mapper;

import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnAttachmentInitRequest;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceAttachments;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceBase;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceContent;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceNotification;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceRequest;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TiltakArrangor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class TilsagnTilAltinnMapper {

    public static final String RESOURCE_ID = "nav_tiltak_tilskuddsbrev";

    private static final String ATTACHMENT_NAME_PREFIX = "Tilskuddsbrev ";
    private static final String FILE_NAME_PREFIX = "Tilskuddsbrev-";
    private static final String FIL_EXT = ".pdf";
    private static final String LANGUAGE_CODE = "nb";
    private static final String MSG_SENDER = "NAV";
    private static final String RECIPIENT_PREFIX = "urn:altinn:organization:identifier-no:";
    private static final String NOTIFICATION_TEMPLATE = "CustomMessage";
    private static final String NOTIFICATION_CHANNEL = "Email";
    private static final String VARSLING_EMNE_PREFIX = "Tilskuddsbrev for ";
    private static final String VARSLING_EMNE_SUFFIX = " er tilgjengelig i Altinn";
    private static final String VARSLING_TEKST_SUFFIX = " er tilgjengelig. Logg inn i Altinn for å se innholdet.";
    private static final String VARSLING_TEKST_FOOTER = "\n\nVennlig hilsen NAV";

    public AltinnCorrespondenceRequest tilAltinnKorrespondanse(final Tilsagn tilsagn) {
        return AltinnCorrespondenceRequest.builder()
            .correspondence(lagKorrespondanseBase(tilsagn))
            .recipients(Collections.singletonList(RECIPIENT_PREFIX + tilsagn.getTiltakArrangor().getOrgNummer()))
            .existingAttachments(Collections.emptyList())
            .idempotentKey(tilsagn.getTilsagnNummer().toString())
            .build();
    }

    public AltinnAttachmentInitRequest tilAltinnVedlegg(final Tilsagn tilsagn) {
        return AltinnAttachmentInitRequest.builder()
            .resourceId(RESOURCE_ID)
            .fileName(lagFilnavn(tilsagn))
            .displayName(vedleggNavn(tilsagn))
            .isEncrypted(false)
            .sendersReference("NAV-" + UUID.randomUUID())
            .build();
    }

    private AltinnCorrespondenceBase lagKorrespondanseBase(Tilsagn tilsagn) {
        OffsetDateTime requestedPublishTime = LocalDateTime.now()
            .atZone(ZoneId.of("UTC"))
            .toOffsetDateTime();
        OffsetDateTime allowSystemDeleteAfter = LocalDate.now().plusYears(10)
            .atTime(LocalTime.MIDNIGHT)
            .atZone(ZoneId.of("UTC"))
            .toOffsetDateTime();
        OffsetDateTime dueDateTime = tilsagn.getRefusjonfristDato()
            .atTime(LocalTime.MIDNIGHT)
            .atZone(ZoneId.of("UTC"))
            .toOffsetDateTime();

        return AltinnCorrespondenceBase.builder()
            .resourceId(RESOURCE_ID)
            .sendersReference("NAV-" + UUID.randomUUID())
            .messageSender(MSG_SENDER)
            .content(lagInnhold(tilsagn))
            .requestedPublishTime(requestedPublishTime)
            .dueDateTime(dueDateTime)
            .notification(lagVarsling(tilsagn.getTiltakArrangor()))
            .allowSystemDeleteAfter(allowSystemDeleteAfter)
            .build();
    }

    private AltinnCorrespondenceContent lagInnhold(Tilsagn tilsagn) {
        return AltinnCorrespondenceContent.builder()
            .language(LANGUAGE_CODE)
            .messageTitle(vedleggNavn(tilsagn))
            .messageSummary(lagSammendrag(tilsagn.getTiltakArrangor()))
            .messageBody(lagMeldingsTekst(tilsagn.getTiltakArrangor()))
            .attachments(List.of(
                AltinnCorrespondenceAttachments.builder()
                    .isEncrypted(false)
                    .sendersReference("NAV-" + UUID.randomUUID())
                    .fileName(lagFilnavn(tilsagn))
                    .displayName(vedleggNavn(tilsagn))
                    .dataLocationType("NewCorrespondenceAttachment")
                    .build()
            ))
            .build();
    }

    private AltinnCorrespondenceNotification lagVarsling(TiltakArrangor tiltakArrangor) {
        String emne = VARSLING_EMNE_PREFIX + tiltakArrangor.getOrgNummer()
            + " " + tiltakArrangor.getArbgiverNavn() + VARSLING_EMNE_SUFFIX;

        return AltinnCorrespondenceNotification.builder()
            .notificationTemplate(NOTIFICATION_TEMPLATE)
            .notificationChannel(NOTIFICATION_CHANNEL)
            .emailSubject(emne)
            .emailBody(lagEpostTekst(tiltakArrangor))
            .sendReminder(false)
            .build();
    }

    private String lagSammendrag(TiltakArrangor tiltakArrangor) {
        return VARSLING_EMNE_PREFIX + tiltakArrangor.getOrgNummer()
            + " " + tiltakArrangor.getArbgiverNavn() + VARSLING_TEKST_SUFFIX;
    }

    private String lagMeldingsTekst(TiltakArrangor tiltakArrangor) {
        return lagSammendrag(tiltakArrangor) + VARSLING_TEKST_FOOTER;
    }

    private String lagEpostTekst(TiltakArrangor tiltakArrangor) {
        return VARSLING_EMNE_PREFIX + tiltakArrangor.getOrgNummer()
            + " " + tiltakArrangor.getArbgiverNavn() + VARSLING_TEKST_SUFFIX + VARSLING_TEKST_FOOTER;
    }

    private String vedleggNavn(Tilsagn tilsagn) {
        StringBuilder sb = new StringBuilder()
            .append(ATTACHMENT_NAME_PREFIX)
            .append(" ")
            .append(tilsagn.getTiltakNavn())
            .append(" ");

        if (tilsagn.erGruppeTilsagn()) {
            return sb.append(tilsagn.getPeriode().getFraDato())
                .append(" til ")
                .append(tilsagn.getPeriode().getTilDato())
                .toString();
        }
        return sb.append(tilsagn.getDeltaker().getEtternavn()).toString();
    }

    private String lagFilnavn(Tilsagn tilsagn) {
        return FILE_NAME_PREFIX + tilsagn.getTiltakNavn() + FIL_EXT;
    }
}
