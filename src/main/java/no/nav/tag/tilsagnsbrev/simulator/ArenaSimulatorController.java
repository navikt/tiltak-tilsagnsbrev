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


    @PostMapping(value = "/kafka")
    public void leggMeldingPaKafkaToppic(@RequestBody String json) throws Exception {
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(json).build();
        tilsagnsbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
    }

    @GetMapping(value = "/ping")
    public String ping() throws Exception {
        return "START";
    }

    @GetMapping(value = "/mq/{tilsagnNr}")
    public String leggMeldingPaMq(@PathVariable String tilsagnNr) throws Exception {
                altInnService.sendTilsagnsbrev(enkeltTilsagn(tilsagnNr).toString());
                return "START";
    }

    private Tilsagn enkeltTilsagn(String tilsagnNr){
        return Tilsagn.builder()
                .tilsagnNummer(new TilsagnNummer("2019", null, tilsagnNr))
                .administrasjonKode("1").antallDeltakere("13").antallTimeverk("500").beslutter(new Person("Besluttesen", "Betsy"))
                .navEnhet(new NavEnhet(null, "NAV Løkka", "Løkka NAV", "Pb 13", "0480", "Oslo", "99999999"))
                .periode(new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2)))
                .build();
    }
}
