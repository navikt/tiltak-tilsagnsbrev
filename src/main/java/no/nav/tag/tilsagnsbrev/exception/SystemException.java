package no.nav.tag.tilsagnsbrev.exception;

public class SystemException extends TilsagnException {

    public SystemException(String errMsg) {
        super(errMsg, false);
    }

    public SystemException(String errMsg, Throwable cause) {
        super(errMsg, false, cause);
    }
}
