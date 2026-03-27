package no.nav.tag.tilsagnsbrev.controller;

import no.nav.security.token.support.core.api.Unprotected;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Unprotected
@RestController
public class HealthCheckController {

    @GetMapping(value = "/internal/healthcheck")
    public String healthcheck() {
        return "START";
    }
}
