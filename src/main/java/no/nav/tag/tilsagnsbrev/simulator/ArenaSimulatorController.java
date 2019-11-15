package no.nav.tag.tilsagnsbrev.simulator;

import no.nav.tag.tilsagnsbrev.Tilsagnsbehandler;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.*;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Profile({"dev", "preprod"})
@RestController
public class ArenaSimulatorController {

    @Autowired
    private Tilsagnsbehandler tilsagnsbehandler;

    @Autowired
    private AltInnService altInnService;


    @PostMapping("kafka")
    public void leggMeldingPaKafkaToppic(@RequestBody String json) throws Exception {
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(json).build();
        tilsagnsbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
    }

    @GetMapping(value = "/ping")
    public String ping() throws Exception {
        return "START";
    }
}
