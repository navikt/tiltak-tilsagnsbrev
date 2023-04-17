package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.DatoUtils;
import no.nav.tag.tilsagnsbrev.behandler.CidManager;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnsbrevBehandler;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.event.ContainerStoppedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Slf4j
@Component
@AllArgsConstructor
public class ArenaConsumer {

    //public static final String topic = "aapen-tiltak-tilsagnsbrevGodkjent-v1";
    public static final String topic = "teamarenanais.aapen-arena-tilsagnsbrevgodkjent-v1";

    private CidManager cidManager;

    @Autowired
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @KafkaListener(topics = topic, errorHandler = "customKafkaErrLogger")
    public void lyttPaArenaTilsagn(ArenaMelding arenaMelding){
        final UUID cid = cidManager.opprettCorrelationId();
        log.debug("Ny tilsagnsbrevmelding fra Arena hentet. Topic: {} Melding: {}", topic, arenaMelding);

        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder()
                .opprettet(DatoUtils.getNow())
                .arenaMelding(arenaMelding)
                .cid(cid).build();
        try {
            tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        } finally {
            cidManager.fjernCorrelationId();
        }
    }
}
