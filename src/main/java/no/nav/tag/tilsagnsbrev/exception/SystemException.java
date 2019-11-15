package no.nav.tag.tilsagnsbrev.exception;

import lombok.Getter;

@Getter
public class SystemException extends TilsagnException {

    public SystemException(String errMsg) {
        super(errMsg);
    }
}
