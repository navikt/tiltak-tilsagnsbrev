package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.TilsagnException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @Builder.Default
    private boolean mappetFraArena = false;
    @Builder.Default
    private int retry = 0;
    @Builder.Default
    boolean datafeil = false;
    @Builder.Default
    private boolean behandlet = false; //Logisk sletting inntil videre

    private LocalDateTime opprettet;
    private Integer tilsagnsbrevId;
    private String journalpostId;
    private Integer altinnKittering;
    private String json;

    @Transient
    private ArenaMelding arenaMelding;
    @Transient
    private Tilsagn tilsagn;

    public boolean skaljournalfoeres(){
        return this.journalpostId == null;
    }

    public boolean erJournalfoert(){
        return this.journalpostId != null;
    }

    public boolean skalTilAltinn(){
        return altinnKittering == null;
    }

    public boolean skalRekjoeres(){
        return retry < MAX_RETRIES;
    }

    public void setRetry(TilsagnException te){
        if(te instanceof DataException){
            retry = MAX_RETRIES;
            return;
        }
        retry += 1;
    }

    public TilsagnUnderBehandling oppdater(TilsagnUnderBehandling ny){
        this.mappetFraArena = ny.mappetFraArena;
        this.retry = ny.retry;
        this.datafeil = ny.datafeil;
        this.journalpostId = ny.journalpostId;
        this.altinnKittering = ny.altinnKittering;
        this.behandlet = ny.behandlet;
        this.json = ny.json;
        return this;
    }
}