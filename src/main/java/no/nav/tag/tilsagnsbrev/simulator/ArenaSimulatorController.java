package no.nav.tag.tilsagnsbrev.simulator;

import no.nav.tag.tilsagnsbrev.Tilsagnsbehandler;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@Profile({"dev", "preprod"})
@RestController
public class ArenaSimulatorController {

    @Autowired
    private Tilsagnsbehandler tilsagnsbehandler;

    @Autowired
    private AltInnService altInnService;

    @Autowired
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;


    @PostMapping(value = "/kafka")
    public void leggMeldingPaKafkaToppic(@RequestBody String json) throws Exception {
        tilsagnsbehandler.behandleTilsagn(json);
    }

    @GetMapping(value = "/ping")
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
