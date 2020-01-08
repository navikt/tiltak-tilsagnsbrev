package no.nav.tag.tilsagnsbrev.exception;

public class SystemException extends TilsagnException {

    public SystemException(String errMsg) {
        super(errMsg, false);
    }
}
