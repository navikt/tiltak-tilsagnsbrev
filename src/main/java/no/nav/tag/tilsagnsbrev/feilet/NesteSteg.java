package no.nav.tag.tilsagnsbrev.feilet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NesteSteg {
    JOURNALFORES("Til Journalføring"),
    TIL_ALTINN("Til Altinn");

    private final String tekst;
}
