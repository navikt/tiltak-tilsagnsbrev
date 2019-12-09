package no.nav.tag.tilsagnsbrev.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class TilsagnException extends RuntimeException{

    private final boolean datafeil;

    public TilsagnException(String errMsg, boolean erDatafeil) {
        super(errMsg);
        this.datafeil = erDatafeil;
    }
}
