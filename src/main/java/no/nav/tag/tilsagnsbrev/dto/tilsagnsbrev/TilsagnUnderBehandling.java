package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TilsagnUnderBehandling {

    public static final int MAX_RETRIES = 3;

    @Id
    private UUID cid;
    @Builder.Default
    private boolean mappetFraArena = false;
    @Builder.Default
    private int retry = 0;
    @Builder.Default
    private boolean datafeil = false;
    @Builder.Default
    private boolean behandlet = false; //Logisk sletting inntil videre

    private LocalDateTime opprettet;
    private Integer tilsagnsbrevId;
    private String journalpostId;
    private String altinnReferanse;
    private String json;
    private byte[] pdfAltinn;
    private byte[] pdfJoark;

    @Transient
    private ArenaMelding arenaMelding;
    @Transient
    private Tilsagn tilsagn;
    @Transient
    private Diskresjonskode diskresjonskode;

    public boolean skaljournalfoeres(){
        return this.journalpostId == null;
    }

    public boolean erJournalfoert(){
        return this.journalpostId != null;
    }

    public boolean skalTilAltinn(){
        return this.altinnReferanse == null;
    }

    public boolean skalRekjoeres(){
        return !this.datafeil && this.retry < MAX_RETRIES;
    }

    public boolean manglerPdf(){
        return this.pdfAltinn == null || this.pdfJoark == null;
    }

    public boolean manglerDiskresjonskode(){
        return this.diskresjonskode == null;
    }

    public void increaseRetry(){
        this.retry += 1;
    }

    public TilsagnUnderBehandling oppdater(TilsagnUnderBehandling ny){
        this.mappetFraArena = ny.mappetFraArena;
        this.retry = ny.retry;
        this.datafeil = ny.datafeil;
        this.journalpostId = ny.journalpostId;
        this.altinnReferanse = ny.altinnReferanse;
        this.behandlet = ny.behandlet;
        this.json = ny.json;
        this.pdfAltinn = ny.pdfAltinn;
        this.pdfJoark = ny.pdfJoark;
        return this;
    }
}
