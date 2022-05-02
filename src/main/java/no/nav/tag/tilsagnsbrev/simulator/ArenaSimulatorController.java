package no.nav.tag.tilsagnsbrev.simulator;

import no.nav.tag.tilsagnsbrev.DatoUtils;
import no.nav.tag.tilsagnsbrev.behandler.CidManager;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnRetryProsess;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnsbrevBehandler;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.integrasjon.ArenaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@Profile({"local", "preprod"})
public class ArenaSimulatorController {

    @Autowired
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @Autowired
    private TilsagnRetryProsess tilsagnRetryProsess;

    @Autowired
    private CidManager cidManager;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping(value = "kafka")
    public String leggMeldingPaKafkaTopic(@RequestBody String arenaJson) throws Exception {
        try {
            kafkaTemplate.send(ArenaConsumer.topic, "TODO", arenaJson);
            return "OK";
        } catch (Exception e){
            throw new RuntimeException("Fungerer ikke i preprod", e);
        }
    }

    @PostMapping(value = "behandler/{tilsagnNr}")
    public String behandle(@PathVariable Integer tilsagnNr, @RequestBody String tilsagnJson) {
        UUID cid = cidManager.opprettCorrelationId();
        ArenaMelding arenaMelding = SimUtil.arenaMelding(tilsagnNr, tilsagnJson);
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(arenaMelding).tilsagnsbrevId(tilsagnNr).cid(cid).opprettet(DatoUtils.getNow()).build();
        try {
            tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tub);
            return "OK";
        }finally {
            cidManager.fjernCorrelationId();
        }
    }

    @GetMapping(value = "retry")
    public String kjoerRetry() throws Exception {
        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();
        return "OK";
    }

    @GetMapping(value = "ping")
    public String ping() throws Exception {
        return "OK";
    }
}
