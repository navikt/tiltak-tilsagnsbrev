package no.nav.tag.tilsagnsbrev.exception;

public class DataException extends TilsagnException {

    public DataException(String errMsg) {
        super(errMsg, true);
    }

    public DataException(String errMsg, Throwable cause) {
        super(errMsg, true, cause);
    }
}
