package no.nav.tag.tilsagnsbrev.controller.exception;

import lombok.Data;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrev;
import no.nav.tag.tilsagnsbrev.feilet.NesteSteg;

@Data
public class DataException extends RuntimeException {

    private NesteSteg nesteSteg;
    private String json;
    private final int retry = FeiletTilsagnsbrev.MAX_RETRIES;

    public DataException(NesteSteg nesteSteg, String json) {
        this.nesteSteg = nesteSteg;
        this.json = json;
    }
}
