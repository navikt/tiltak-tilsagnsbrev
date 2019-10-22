package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.Tilsagnsbehandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Data
@Slf4j
public class ArenaTilsagnLytter {

    @Autowired
    private Tilsagnsbehandler tilsagnsbehandler;

    public static final String group = "tiltak-tilsagnsbrev";
    public static final String topic = "aapen-tiltak-tilsagnsbrevGodkjent-v1";

    private CountDownLatch latch; //For testing

    @KafkaListener(groupId = group, topics = topic)
    public void lyttPaArenaTilsagn(ConsumerRecord<String, String> tilsagnsMelding){

        log.info("Henter melding med id {}", tilsagnsMelding.key());

        try {
            tilsagnsbehandler.behandleTilsagn(tilsagnsMelding.value());
        } finally {
            this.countdownLatch();
        }
        log.info("Tilsagn: {}", tilsagnsMelding); //TODO ta vekk før prod
    }

    private void countdownLatch(){
        if(latch != null){
            latch.countDown();
        }
    }

}
