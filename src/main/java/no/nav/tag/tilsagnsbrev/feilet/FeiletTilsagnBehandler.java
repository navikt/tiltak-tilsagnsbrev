package no.nav.tag.tilsagnsbrev.feilet;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.TilsagnException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FeiletTilsagnBehandler {

    @Autowired
    FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    public List<TilsagnUnderBehandling> hentAlleTilRekjoring() {
        return feiletTilsagnsbrevRepository.findAll().stream().filter(TilsagnUnderBehandling::skalRekjoeres).collect(Collectors.toList());
    }

    public boolean lagreEllerOppdaterFeil(TilsagnUnderBehandling tilsagnUnderBehandling, Exception e) {
        if (e instanceof TilsagnException) {
            TilsagnException te = (TilsagnException) e;
            tilsagnUnderBehandling.setRetry(te);
            return lagreEllerOppdater(tilsagnUnderBehandling);
        }
        return false;
    }

    public boolean oppdater(TilsagnUnderBehandling oppdatertTilsagn, Exception e) {
        return oppdaterFeilet(oppdatertTilsagn, Optional.of(e));
    }

    public boolean oppdater(TilsagnUnderBehandling oppdatertTilsagn) {
        return oppdaterFeilet(oppdatertTilsagn, Optional.empty());
    }

    private boolean oppdaterFeilet(TilsagnUnderBehandling oppdatertTilsagn, Optional<Exception> optEx) {
        return feiletTilsagnsbrevRepository.findById(oppdatertTilsagn.getCid())
                .map(hentet -> hentet.oppdater(oppdatertTilsagn))
                .map(oppdatert -> lagreEllerOppdater(oppdatert))
                .orElseThrow(() -> new RuntimeException("Fant ikke feilet tilsagnsbrev i database: " + oppdatertTilsagn.getCid()));
    }

    private boolean lagreEllerOppdater(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            TilsagnUnderBehandling oppdatert = feiletTilsagnsbrevRepository
                    .findById(tilsagnUnderBehandling.getCid())
                    .map(tub -> tub.oppdater(tilsagnUnderBehandling))
                    .orElse(tilsagnUnderBehandling);
            feiletTilsagnsbrevRepository.save(oppdatert);
            return true;
        } catch (Exception e) {
            log.error("Feil ved lagring av tilsagnsfeil! Tilsagn: {}", tilsagnUnderBehandling.getJson(), e);
            throw new RuntimeException(e);
        }
    }

    public void slettTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        log.info("Fjerner tilsagn fra database: {}", tilsagnUnderBehandling.getTilsagn());
        feiletTilsagnsbrevRepository.delete(tilsagnUnderBehandling);
    }
}

