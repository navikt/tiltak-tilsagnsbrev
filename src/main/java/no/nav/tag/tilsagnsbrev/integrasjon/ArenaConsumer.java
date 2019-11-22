package no.nav.tag.tilsagnsbrev.integrasjon;

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
@Profile("kafka")
public class ArenaConsumer {

    @Autowired
    private CidManager cidManager;

    @Autowired
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    public static final String topic = "aapen-tiltak-tilsagnsbrevGodkjent-v1";

    private CountDownLatch latch;

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
            this.countdownLatch();
        }
    }

    private void countdownLatch() {
        if (latch != null) {
            latch.countDown();
        }
    }
}
