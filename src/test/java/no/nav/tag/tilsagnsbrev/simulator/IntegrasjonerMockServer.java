package no.nav.tag.tilsagnsbrev.simulator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
@Profile("local")
@Slf4j
@Getter
public class IntegrasjonerMockServer implements DisposableBean {
    private final WireMockServer server;
    private final String pdfFil = EncodedString.ENC_STR;
    private static final String ATTACHMENT_ID = UUID.randomUUID().toString();
    private static final String CORRESPONDENCE_ID = UUID.randomUUID().toString();

    public IntegrasjonerMockServer() {
        log.info("Starter mockserver for eksterne integrasjoner.");
        server = new WireMockServer(WireMockConfiguration.options().usingFilesUnderClasspath(".").port(8090));
        server.start();
        stubForAltOk();
    }

    @Override
    public void destroy() {
        log.info("Stopper mockserver.");
        server.stop();
    }

    public void stubForAltOk() {
        // Joark
        server.stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true")
                .willReturn(okJson("{\"journalpostId\" : \"001\", \"journalstatus\" : \"MIDLERTIDIG\", \"melding\" : \"Gikk bra\"}")));

        // PDF-generering
        server.stubFor(post(urlPathMatching("/template/.*/create-pdf"))
                .willReturn(okJson("{\"pdf\" : \"" + pdfFil + "\"}")));

        // Altinn 3 — initialiser vedlegg
        server.stubFor(post("/correspondence/api/v1/attachment")
                .willReturn(okJson("\"" + ATTACHMENT_ID + "\"")));

        // Altinn 3 — last opp vedleggdata
        server.stubFor(post(urlPathMatching("/correspondence/api/v1/attachment/.*/upload"))
                .willReturn(okJson("{\"attachmentId\" : \"" + ATTACHMENT_ID + "\", \"status\" : \"UploadProcessing\"}")));

        // Altinn 3 — opprett korrespondanse
        server.stubFor(post("/correspondence/api/v1/correspondence")
                .willReturn(okJson("{\"correspondences\": [{\"correspondenceId\": \"" + CORRESPONDENCE_ID + "\", \"status\": \"Initialized\", \"recipient\": \"urn:altinn:organization:identifier-no:123456789\"}], \"attachmentIds\": [\"" + ATTACHMENT_ID + "\"]}")));

        // Maskinporten token (lokal mock)
        server.stubFor(post(urlPathMatching(".*/token.*"))
                .willReturn(okJson("{\"access_token\": \"mock-token\", \"token_type\": \"Bearer\", \"expires_in\": 120, \"scope\": \"altinn:serviceowner altinn:correspondence.write\"}")));
    }
}
