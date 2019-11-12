package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.Tilsagnsbehandler;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Component
@Data
@Slf4j
@Profile("dev")
public class ArenaConsumer {

    public static final String CID = "CID";

    @Autowired
    private Tilsagnsbehandler tilsagnsbehandler;

    public static final String group = "tiltak-tilsagnsbrev-1";
    public static final String topic = "aapen-tiltak-tilsagnsbrevGodkjent-v1";

    private CountDownLatch latch; //For testing

    @KafkaListener(groupId = group, topics = topic)
    public void lyttPaArenaTilsagn(ConsumerRecord<String, String> tilsagnsMelding) {

        log.debug("Henter melding {}", tilsagnsMelding.value()); //TODO ta vekk før prod
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().opprettet(LocalDateTime.now()).cid(opprettCorrelationId(tilsagnsMelding.key())).json(tilsagnsMelding.value()).build();

        try {
            tilsagnsbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        } finally {
            this.countdownLatch();
        }
    }

    private void countdownLatch() {
        if (latch != null) {
            latch.countDown();
        }
    }

    private UUID opprettCorrelationId(String key) { //TODO Hør med Arena om de kan sende cid
        final UUID cid;
        if (key.contains(CID)) {
            cid = UUID.fromString(key.substring(CID.length()));
        } else {
            cid = UUID.randomUUID();
        }
        MDC.put(CID, cid.toString());
        return cid;
    }

}
