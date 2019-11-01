package no.nav.tag.tilsagnsbrev.simulator;

import no.nav.tag.tilsagnsbrev.TilsagnBuilder;
import no.nav.tag.tilsagnsbrev.Tilsagnsbehandler;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.*;
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
        tilsagnsbehandler.behandleTilsagn(json);
    }

    @GetMapping(value = "/ping")
    public String ping() throws Exception {
        return "OK";
    }

    @GetMapping(value = "/mq/{tilsagnNr}")
    public String leggMeldingPaMq(@PathVariable String tilsagnNr) throws Exception {
                altInnService.sendTilsagnsbrev(enkeltTilsagn(tilsagnNr).toString());
                return "OK";
    }

    private Tilsagn enkeltTilsagn(String tilsagnNr){
        return new TilsagnBuilder()
                .medTilsagnNummer(new TilsagnNummer("2019", null, tilsagnNr))
                .medAdministrasjonKode("1").medAntallDeltakere("13").medAntallTimeverk("500").medBeslutter(new Person("Besluttesen", "Betsy"))
                .medNavEnhet(new NavEnhet(null, "NAV Løkka", "Løkka NAV", "Pb 13", "0480", "Oslo", "99999999"))
                .medPeriode(new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2)))
                .createTilsagn();
    }
}
