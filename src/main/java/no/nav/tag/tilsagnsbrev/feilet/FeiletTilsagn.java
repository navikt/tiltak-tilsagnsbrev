package no.nav.tag.tilsagnsbrev.feilet;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.JOURNALFORES;
import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.TIL_ALTINN;

@Data
@AllArgsConstructor
public class FeiletTilsagn {

    private UUID id;
    private LocalDateTime opprettet;
    private NesteSteg nesteSteg;
    private int retry;
    private String tilsagnJson;

    public boolean skaljournalfoeres(){
        return this.getNesteSteg().equals(JOURNALFORES);
    }

    public boolean skalTilAltinn(){
        return this.getNesteSteg().equals(TIL_ALTINN);
    }
}
