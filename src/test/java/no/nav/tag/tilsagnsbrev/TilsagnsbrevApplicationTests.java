package no.nav.tag.tilsagnsbrev;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static no.nav.tag.tilsagnsbrev.integrasjon.ArenaConsumer.topic;

@SpringBootTest
@ActiveProfiles("local")
@EmbeddedKafka(partitions = 1, topics = topic)
public class TilsagnsbrevApplicationTests {

    @Test
    public void contextLoads() {
    }
}
