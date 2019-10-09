package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Tilsagnsbehandler {

    @Autowired
    TilsagnJsonMapper tilsagnJsonMapper;

    public void behandleTilsagn(String tilsagnJson) {
            final Tilsagn tilsagn = tilsagnJsonMapper.jsonTilTilsagn(tilsagnJson);
            log.info("Behandler tilsagn {}", tilsagn.getTilsagnNummer().getLoepenrTilsagn());

    }
}
