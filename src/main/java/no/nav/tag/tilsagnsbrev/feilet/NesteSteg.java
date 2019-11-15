package no.nav.tag.tilsagnsbrev.feilet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NesteSteg {
    START(0, "Start"),
 //   HENT_PDF(3, "Hent pdf"),
 //   LAG_JOURNALPOST(4, "Opprett journalpost"),
    JOURNALFOER(5, "Journalføres"),
 //   LAG_ALTINN_XML(6, "Opprett Altinn xml"),
    TIL_ALTINN(7, "Til Altinn");

    private final int stegNr;
    private final String tekst;
}
