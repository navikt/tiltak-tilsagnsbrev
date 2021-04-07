package no.nav.tag.tilsagnsbrev.simulator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
@Profile("local")
@Slf4j
@Getter
public class IntegrasjonerMockServer implements DisposableBean {
    private final WireMockServer server;
    private final String altinnOkRespons = SimUtil.lesFil("simulator/altinn200Resp.xml");
    private final String pdfFil = EncodedString.ENC_STR;

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
        server.stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true")
                .willReturn(okJson("{\"journalpostId\" : \"001\", \"journalstatus\" : \"MIDLERTIDIG\", \"melding\" : \"Gikk bra\"}")));
        server.stubFor(post("/template/tiltak-tilsagnsbrev-midlertidig-lonnstilskudd/create-pdf").willReturn(okJson("{\"pdf\" : \"" + pdfFil + "\"}")));
        server.stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(ok().withBody(altinnOkRespons)));
//        server.stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(serverError()));
    }
}
