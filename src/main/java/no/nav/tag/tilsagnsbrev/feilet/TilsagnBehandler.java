package no.nav.tag.tilsagnsbrev.feilet;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.TilsagnException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TilsagnBehandler {

    @Autowired
    private TilsagnsbrevRepository tilsagnsbrevRepository;

    public List<TilsagnUnderBehandling> hentAlleTilRekjoring() {
        return tilsagnsbrevRepository.findAll().stream().filter(tub -> !tub.isBehandlet()).filter(TilsagnUnderBehandling::skalRekjoeres).collect(Collectors.toList());
    }

    public boolean lagreEllerOppdaterFeil(TilsagnUnderBehandling tilsagnUnderBehandling, Exception e) {
        if (e instanceof TilsagnException) {
            TilsagnException te = (TilsagnException) e;
            tilsagnUnderBehandling.setRetry(te);
            tilsagnUnderBehandling.setDatafeil(te.isDatafeil());
            return lagreEllerOppdater(tilsagnUnderBehandling);
        }
        return false;
    }

    public boolean lagreStatus(TilsagnUnderBehandling oppdatertTilsagn) {
        return lagreEllerOppdater(oppdatertTilsagn);
    }

    private boolean lagreEllerOppdater(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            TilsagnUnderBehandling oppdatertTilsagnUnderBehandling = tilsagnsbrevRepository
                    .findById(tilsagnUnderBehandling.getCid())
                    .map(tub -> tub.oppdater(tilsagnUnderBehandling))
                    .orElse(tilsagnUnderBehandling);
            tilsagnsbrevRepository.save(oppdatertTilsagnUnderBehandling);
            return true;
        } catch (Exception e) {
            log.error("Feil ved lagring av tilsagnsfeil! Tilsagn: {}", tilsagnUnderBehandling.getJson(), e);
            throw new RuntimeException(e);
        }
    }

    public void slettTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        log.info("Fjerner tilsagn fra database: {}", tilsagnUnderBehandling.getTilsagn());
        tilsagnsbrevRepository.delete(tilsagnUnderBehandling);
    }
}

