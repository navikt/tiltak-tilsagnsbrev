package no.nav.tag.tilsagnsbrev.exception;

import lombok.Data;

import static no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling.MAX_RETRIES;

@Data
public class DataException extends TilsagnException {

    private final int retry = MAX_RETRIES;

    public DataException(String errMsg) {
        super(errMsg, true);
    }
}
