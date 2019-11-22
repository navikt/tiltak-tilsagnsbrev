package no.nav.tag.tilsagnsbrev.simulator;

import no.nav.tag.tilsagnsbrev.CidManager;
import no.nav.tag.tilsagnsbrev.TilsagnsbrevBehandler;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Profile({"dev", "preprod"})
@RestController
public class ArenaSimulatorController {

    @Autowired
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @Autowired
    private AltInnService altInnService;

    @Autowired
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    @Autowired
    private CidManager cidManager;


    @PostMapping(value = "kafka")
    public void leggMeldingPaKafkaToppic(@RequestBody String json) throws Exception {
        try {
            UUID cid = cidManager.opprettCorrelationId();
            TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().cid(cid).json(json).build();
            tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        }finally {
            cidManager.fjernCorrelationId();
        }
    }

    @GetMapping(value = "ping")
    public String ping() throws Exception {
        return "OK";
    }

    @GetMapping("altinn/{tilsagnNr}")
    public String sendTilAltinn(@PathVariable String tilsagnNr) throws Exception {
        byte[] pdf = EncodedString.getEncAsBytes();
        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
        altInnService.sendTilsagnsbrev(tilsagnTilAltinnMapper.tilAltinnMelding(tilsagn, pdf));
        return "OK";
    }
}
