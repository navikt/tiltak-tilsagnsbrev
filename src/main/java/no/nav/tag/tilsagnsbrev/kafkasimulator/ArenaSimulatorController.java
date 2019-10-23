package no.nav.tag.tilsagnsbrev.kafkasimulator;

import no.nav.tag.tilsagnsbrev.Tilsagnsbehandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/tilsagn")
public class ArenaSimulatorController {

    @Autowired
    Tilsagnsbehandler tilsagnsbehandler;

    @PostMapping
    public void leggMeldingPaToppic(@RequestBody String json) throws Exception {
        tilsagnsbehandler.behandleTilsagn(json);

    }

}
