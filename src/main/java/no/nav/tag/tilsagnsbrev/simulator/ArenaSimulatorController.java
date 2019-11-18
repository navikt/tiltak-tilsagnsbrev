package no.nav.tag.tilsagnsbrev.simulator;

import no.nav.tag.tilsagnsbrev.Tilsagnsbrevbehandler;
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
    private Tilsagnsbrevbehandler tilsagnsbrevbehandler;

    @Autowired
    private AltInnService altInnService;

    @Autowired
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;


    @PostMapping(value = "kafka")
    public void leggMeldingPaKafkaToppic(@RequestBody String json) throws Exception {
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().cid(UUID.randomUUID()).json(json).build();
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
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
