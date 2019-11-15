package no.nav.tag.tilsagnsbrev.simulator;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Slf4j
@Component
@Getter
public class IntegrasjonerMockServer implements DisposableBean {
    private final WireMockServer server;

    public IntegrasjonerMockServer() {
        log.info("Starter mockserver for eksterne integrasjoner.");
        server = new WireMockServer(WireMockConfiguration.options().usingFilesUnderClasspath(".").port(8090));
        server.start();
    }

    public void start(){
        server.start();
    }

    @Override
    public void destroy() {
        log.info("Stopper mockserver.");
        server.stop();
    }
}