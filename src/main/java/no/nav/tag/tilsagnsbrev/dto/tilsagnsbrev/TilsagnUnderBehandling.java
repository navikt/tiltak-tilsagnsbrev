package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.TilsagnException;
import no.nav.tag.tilsagnsbrev.feilet.NesteSteg;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.UUID;

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TilsagnUnderBehandling {

    public static final int MAX_RETRIES = 4;
    static final int FEILMELDING_MAXLENGTH = 255;

    @Id
    private UUID cid;
    private String feilmelding;
    @Builder.Default
    private LocalDateTime opprettet = LocalDateTime.now();
    @Builder.Default
    private NesteSteg nesteSteg = START;
    private int retry;
    private String json;
    @Transient
    private Tilsagn tilsagn;

    public boolean erDefualt(){
        return nesteSteg.equals(START);
    }

    public boolean skaljournalfoeres(){
        return this.getNesteSteg().equals(JOURNALFOER);
    }

    public boolean skalTilAltinn(){
        return this.getNesteSteg().equals(TIL_ALTINN);
    }

    public boolean skalRekjoeres(){
        return retry < MAX_RETRIES;
    }

    public void setFeilmelding(String feilmelding){
        if(feilmelding != null && feilmelding.length() > FEILMELDING_MAXLENGTH){
            this.feilmelding = feilmelding.substring(0, FEILMELDING_MAXLENGTH);
            return;
        }
        this.feilmelding = feilmelding;
    }

    public void setRetry(TilsagnException te){
        if(te instanceof DataException){
            retry = MAX_RETRIES;
            return;
        }
        retry += 1;
    }
}
