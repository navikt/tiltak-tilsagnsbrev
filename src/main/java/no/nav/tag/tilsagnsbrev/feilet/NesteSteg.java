package no.nav.tag.tilsagnsbrev.feilet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NesteSteg {
    JOURNALFORES("Journalføres"),
    TIL_ALTINN("Til Altinn"),
    OK("OK");

    private final String tekst;
}
