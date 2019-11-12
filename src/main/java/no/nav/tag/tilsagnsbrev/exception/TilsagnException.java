package no.nav.tag.tilsagnsbrev.exception;

import lombok.Data;

@Data
public abstract class TilsagnException extends RuntimeException{

    protected TilsagnException(String errMsg) {
        super(errMsg);
    }
}
