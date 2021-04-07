package no.nav.tag.tilsagnsbrev.behandler;

import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeilRapport {

    @Counted(value = "retry.fail", description = "Tilsagn må behandles manuelt")
    void varsleProsessfeil(TilsagnUnderBehandling feiletTilsagnsbrev) {
        log.warn("Tilsagnsbrev må behandlers manuelt: OrgNummer={}, JournalpostId={}, tiltak={}"
                , feiletTilsagnsbrev.getTilsagn().getTiltakArrangor().getOrgNummer()
                , feiletTilsagnsbrev.getJournalpostId()
                , feiletTilsagnsbrev.getTilsagn().getTiltakNavn());
    }
}
