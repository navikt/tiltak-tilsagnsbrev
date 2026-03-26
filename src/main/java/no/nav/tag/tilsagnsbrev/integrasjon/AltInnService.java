package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnAttachmentInitRequest;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceRequest;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceResponse;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnVedleggStatusResponse;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.integrasjon.maskinporten.MaskinportenService;
import no.nav.tag.tilsagnsbrev.konfigurasjon.altinn.AltinnProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
public class AltInnService {

    private static final String ATTACHMENT_PATH = "/correspondence/api/v1/attachment";
    private static final String ATTACHMENT_UPLOAD_PATH = "/correspondence/api/v1/attachment/{attachmentId}/upload";
    private static final String ATTACHMENT_STATUS_PATH = "/correspondence/api/v1/attachment/{attachmentId}";
    private static final String CORRESPONDENCE_PATH = "/correspondence/api/v1/correspondence";

    private static final String ATTACHMENT_STATUS_PUBLISHED = "Published";
    private static final String ATTACHMENT_STATUS_FAILED = "Failed";
    private static final int ATTACHMENT_POLL_INTERVAL_MS = 2000;
    private static final int ATTACHMENT_POLL_MAX_ATTEMPTS = 30;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MaskinportenService maskinportenService;

    @Autowired
    private AltinnProperties altinnProperties;

    public String sendTilsagnsbrev(AltinnCorrespondenceRequest correspondenceRequest) {
        try {
            return opprettKorrespondanse(correspondenceRequest);
        } catch (DataException | SystemException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemException("Uventet feil ved sending til Altinn 3: " + e.getMessage());
        }
    }

    public UUID sendVedlegg(AltinnAttachmentInitRequest attachmentInitRequest, byte[] pdf) {
        UUID attachmentId = initialiserVedlegg(attachmentInitRequest);
        lastOppVedlegg(attachmentId, pdf);
        ventTilVedleggErPublisert(attachmentId);
        return attachmentId;
    }

    private UUID initialiserVedlegg(AltinnAttachmentInitRequest request) {
        try {
            return restTemplate.postForObject(
                altinnProperties.getBaseUrl() + ATTACHMENT_PATH,
                jsonEntityMedToken(request),
                UUID.class
            );
        } catch (HttpClientErrorException e) {
            throw new DataException("Altinn 3 avviste vedlegget (4xx): " + e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new SystemException("Altinn 3 serverfeil ved initialisering av vedlegg (5xx): " + e.getMessage());
        }
    }

    private void lastOppVedlegg(UUID attachmentId, byte[] pdf) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setBearerAuth(maskinportenService.hentToken());

            restTemplate.postForObject(
                altinnProperties.getBaseUrl() + ATTACHMENT_UPLOAD_PATH,
                new HttpEntity<>(pdf, headers),
                Void.class,
                attachmentId
            );
        } catch (HttpClientErrorException e) {
            throw new DataException("Altinn 3 avviste vedlegg-opplasting (4xx): " + e.getMessage());
        } catch (HttpServerErrorException e) {
            maskinportenService.evict();
            throw new SystemException("Altinn 3 serverfeil ved opplasting av vedlegg (5xx): " + e.getMessage());
        }
    }

    private void ventTilVedleggErPublisert(UUID attachmentId) {
        for (int attempt = 1; attempt <= ATTACHMENT_POLL_MAX_ATTEMPTS; attempt++) {
            try {
                    AltinnVedleggStatusResponse response = restTemplate.exchange(
                    altinnProperties.getBaseUrl() + ATTACHMENT_STATUS_PATH,
                    HttpMethod.GET,
                    new HttpEntity<>(bearerAuthHeaders()),
                    AltinnVedleggStatusResponse.class,
                    attachmentId
                ).getBody();

                if (response == null) {
                    throw new SystemException("Tomt svar fra Altinn 3 vedlegg-status-API for vedlegg " + attachmentId);
                }

                String status = response.getStatus();
                log.debug("Vedlegg {} status: {} (forsøk {}/{})", attachmentId, status, attempt, ATTACHMENT_POLL_MAX_ATTEMPTS);

                if (ATTACHMENT_STATUS_PUBLISHED.equals(status)) {
                    return;
                }
                if (ATTACHMENT_STATUS_FAILED.equals(status)) {
                    throw new DataException("Altinn 3 feilet under virusskanning av vedlegg " + attachmentId + " (status: " + status + ")");
                }

                Thread.sleep(ATTACHMENT_POLL_INTERVAL_MS);
            } catch (DataException | SystemException e) {
                throw e;
            } catch (HttpClientErrorException e) {
                throw new DataException("Altinn 3 avviste vedlegg-status-forespørsel (4xx): " + e.getMessage());
            } catch (HttpServerErrorException e) {
                throw new SystemException("Altinn 3 serverfeil ved henting av vedlegg-status (5xx): " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SystemException("Avbrutt mens vi ventet på at vedlegg " + attachmentId + " skulle bli publisert");
            }
        }
        throw new SystemException("Tidsavbrudd: vedlegg " + attachmentId + " ble ikke publisert etter " + ATTACHMENT_POLL_MAX_ATTEMPTS + " forsøk");
    }

    private String opprettKorrespondanse(AltinnCorrespondenceRequest request) {
        try {
            AltinnCorrespondenceResponse response = restTemplate.postForObject(
                altinnProperties.getBaseUrl() + CORRESPONDENCE_PATH,
                jsonEntityMedToken(request),
                AltinnCorrespondenceResponse.class
            );

            if (response == null || response.getCorrespondences() == null || response.getCorrespondences().isEmpty()) {
                throw new SystemException("Tomt svar fra Altinn 3 korrespondanse-API");
            }

            return response.getCorrespondences().get(0).getCorrespondenceId().toString();
        } catch (DataException | SystemException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            throw new DataException("Altinn 3 avviste korrespondansen (4xx): " + e.getMessage());
        } catch (HttpServerErrorException e) {
            maskinportenService.evict();
            throw new SystemException("Altinn 3 serverfeil ved oppretting av korrespondanse (5xx): " + e.getMessage());
        }
    }

    private <T> HttpEntity<T> jsonEntityMedToken(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(maskinportenService.hentToken());
        return new HttpEntity<>(body, headers);
    }

    private HttpHeaders bearerAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(maskinportenService.hentToken());
        return headers;
    }
}
