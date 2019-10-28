package no.nav.tag.tilsagnsbrev.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping(value = "/internal/healthcheck")
    public String healthcheck() {
        return "OK";
    }
}
