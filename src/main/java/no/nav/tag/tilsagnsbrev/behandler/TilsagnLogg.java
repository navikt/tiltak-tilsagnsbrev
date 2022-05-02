package no.nav.tag.tilsagnsbrev.behandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.nav.tag.tilsagnsbrev.DatoUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TilsagnLogg {
    @Id
    private UUID id;
    private Integer tilsagnsbrevId;
    private LocalDateTime tidspunktLest = DatoUtils.getNow();

    public TilsagnLogg(UUID cid, Integer tilsagnsbrevId) {
        this.id = cid;
        this.tilsagnsbrevId = tilsagnsbrevId;
    }
}
