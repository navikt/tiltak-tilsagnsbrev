package no.nav.tag.tilsagnsbrev.feilet;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.exception.TilsagnException;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeiletTilsagnBehandler {

    @Autowired
    FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    public boolean lagreFeil(TilsagnUnderBehandling tilsagnUnderBehandling, Exception e) {
        if(e instanceof TilsagnException){
            TilsagnException te = (TilsagnException)e;
            tilsagnUnderBehandling.setFeilmelding(e.getMessage());
            tilsagnUnderBehandling.setRetry(te);
            return lagre(tilsagnUnderBehandling);
        }
        return false;
    }


    private boolean lagre(TilsagnUnderBehandling tilsagnUnderBehandling){
        try {
            feiletTilsagnsbrevRepository.save(tilsagnUnderBehandling);
            return true;
        }catch (Exception e){
            log.error("Feil ved lagring av tilsagnsfeil! Tilsagn: {}", tilsagnUnderBehandling.getJson(), e);
            throw new RuntimeException(e);
        }
    }

}
