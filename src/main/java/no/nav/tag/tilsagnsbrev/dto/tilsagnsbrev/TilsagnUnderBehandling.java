package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private LocalDateTime opprettet = LocalDateTime.now();
    @Builder.Default
    private boolean mappetFraArena = false;
    @Builder.Default
    private int retry = 0;
    @Builder.Default
    boolean datafeil = false;
    @Builder.Default
    private boolean behandlet = false; //Logisk sletting inntil videre

    private String journalpostId;
    private Integer altinnKittering;

    private String feilmelding;
    private String json;

    @Transient
    private Tilsagn tilsagn;
    @Transient
    private Gson gson = new Gson();

    public boolean fraStart(){
        return this.mappetFraArena == false;
    }

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

    public boolean skalMappesFraArenaMelding(){
        return !isMappetFraArena();
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

    public void opprettTilsagn(){
        if(this.tilsagn != null){
            return;
        }
        if(!this.isMappetFraArena()){
            throw new RuntimeException("Kan ikke opprette tilsagn fra en ubehandlet Arenamelding");
        }
        this.tilsagn = gson.fromJson(this.json, Tilsagn.class);
    }

    public TilsagnUnderBehandling oppdater(TilsagnUnderBehandling ny){
        this.mappetFraArena = ny.mappetFraArena;
        this.retry = ny.retry;
        this.datafeil = ny.datafeil;
        this.journalpostId = ny.journalpostId;
        this.altinnKittering = ny.altinnKittering;
        this.feilmelding = ny.feilmelding;
        this.behandlet = ny.behandlet;
        this.json = ny.json;
        return this;
    }
}