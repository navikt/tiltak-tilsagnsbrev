package no.nav.tag.tilsagnsbrev.feilet;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.JOURNALFORES;
import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.TIL_ALTINN;

@Data
@AllArgsConstructor
@Entity
public class FeiletTilsagnsbrev {

    private static final int MAX_RETRIES = 3;

    @Id
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

    public boolean skalRekjoeres(){
        return retry < MAX_RETRIES;
    }
}
