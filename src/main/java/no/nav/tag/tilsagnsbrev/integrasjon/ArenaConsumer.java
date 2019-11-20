package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.Tilsagnsbrevbehandler;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Data
@Slf4j
@Component
@Profile("kafka")
public class ArenaConsumer {

    public static final String CID = "CID";

    @Autowired
    private Tilsagnsbrevbehandler tilsagnsbrevbehandler;

    public static final String group = "tiltak-tilsagnsbrev-2";
    public static final String topic = "aapen-tiltak-tilsagnsbrevGodkjent-v1";

    private CountDownLatch latch;

    @KafkaListener(groupId = group, topics = topic)
    public void lyttPaArenaTilsagn(ConsumerRecord<String, String> tilsagnsMelding){

        log.info("Ny melding hentet fra topic");
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().opprettet(LocalDateTime.now()).cid(opprettCorrelationId(tilsagnsMelding.key())).json(tilsagnsMelding.value()).build();
        try {
            tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        } finally {
            fjernCorrelationId();
            this.countdownLatch();
        }
    }

    private void countdownLatch() {
        if (latch != null) {
            latch.countDown();
        }
    }

    private UUID opprettCorrelationId(String key) { //TODO Hør med Arena om de kan sende cid
        final UUID cid = UUID.randomUUID();
        MDC.put(CID, cid.toString());
        return cid;
    }

    private void fjernCorrelationId(){
        Optional.ofNullable(MDC.get(CID)).ifPresent(cid -> MDC.remove(cid));
    }
}
