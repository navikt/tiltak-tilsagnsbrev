package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnAttachmentInitRequest;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceRequest;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceResponse;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.integrasjon.maskinporten.MaskinportenService;
import no.nav.tag.tilsagnsbrev.konfigurasjon.altinn.AltinnProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
    private static final String CORRESPONDENCE_PATH = "/correspondence/api/v1/correspondence";

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
}
