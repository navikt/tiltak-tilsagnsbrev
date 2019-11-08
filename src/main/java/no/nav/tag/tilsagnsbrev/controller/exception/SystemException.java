package no.nav.tag.tilsagnsbrev.controller.exception;

import no.nav.tag.tilsagnsbrev.feilet.NesteSteg;

public class SystemException extends RuntimeException {

    private NesteSteg nesteSteg;
    private String json;
    private final int retry = 0;

    public SystemException(NesteSteg nesteSteg, String json) {
        this.nesteSteg = nesteSteg;
        this.json = json;
    }
}
