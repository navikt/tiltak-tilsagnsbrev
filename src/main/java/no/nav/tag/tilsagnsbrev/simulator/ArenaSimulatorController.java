package no.nav.tag.tilsagnsbrev.simulator;

import no.nav.tag.tilsagnsbrev.behandler.CidManager;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnRetryProsess;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnsbrevBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.ArenaConsumer;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile({"dev", "preprod"})
public class ArenaSimulatorController {

    @Autowired
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @Autowired
    private TilsagnRetryProsess tilsagnRetryProsess;

    @Autowired
    private AltInnService altInnService;

    @Autowired
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    @Autowired
    private CidManager cidManager;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @PostMapping(value = "kafka")
    public void leggMeldingPaKafkaTopic(@RequestBody String arenaJson) throws Exception {
            kafkaTemplate.send(ArenaConsumer.topic, "TODO", arenaJson);
    }

    @GetMapping(value = "retry")
    public void kjoerRetry(@RequestBody String json) throws Exception {
        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();
    }

    @GetMapping(value = "ping")
    public String ping() throws Exception {
        return "OK";
    }
}
