package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.CidManager;
import no.nav.tag.tilsagnsbrev.TilsagnsbrevBehandler;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

@Data
@Slf4j
@Component
@Profile("dev")
@AllArgsConstructor
public class ArenaConsumer {

    public static final String topic = "aapen-tiltak-tilsagnsbrevGodkjent-v1";

    private CidManager cidManager;
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @KafkaListener(topics = topic)
    public void lyttPaArenaTilsagn(ConsumerRecord<String, String> tilsagnsMelding){
        cidManager.opprettCorrelationId();
        log.info("Ny melding hentet fra topic");
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder()
                .opprettet(LocalDateTime.now())
                .cid(cidManager.opprettCorrelationId())
                .json(tilsagnsMelding.value()).build();
        try {
            tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        } finally {
            cidManager.fjernCorrelationId();
        }
    }
}
