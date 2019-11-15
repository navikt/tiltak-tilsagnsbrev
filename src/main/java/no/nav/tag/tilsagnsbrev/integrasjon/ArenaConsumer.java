package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.Tilsagnsbehandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Data
@Slf4j
@Component
@Profile("kafka")
public class ArenaConsumer {

    @Autowired
    private Tilsagnsbehandler tilsagnsbehandler;

    public static final String group = "tiltak-tilsagnsbrev-2";
    public static final String topic = "aapen-tiltak-tilsagnsbrevGodkjent-v1";

    private CountDownLatch latch; //For testing

    @KafkaListener(groupId = group, topics = topic)
    public void lyttPaArenaTilsagn(ConsumerRecord<String, String> tilsagnsMelding){
        try {
            tilsagnsbehandler.behandleTilsagn(tilsagnsMelding.value());
        } finally {
            this.countdownLatch();
        }
    }

    private void countdownLatch(){
        if(latch != null){
            latch.countDown();
        }
    }

}
