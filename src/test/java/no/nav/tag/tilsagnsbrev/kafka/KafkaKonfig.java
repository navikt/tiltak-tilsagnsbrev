package no.nav.tag.tilsagnsbrev.kafka;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.integrasjon.ArenaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

@Slf4j
@Configuration
@Profile("local-app")
public class KafkaKonfig {

    @Autowired
    public EmbeddedKafkaBroker embeddedKafkaBroker() {
        log.info("Starter embedded Kafka");
        EmbeddedKafkaBroker embeddedKafka = new EmbeddedKafkaBroker(1, true, ArenaConsumer.topic);
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
        embeddedKafka.afterPropertiesSet();
        return embeddedKafka;
    }

    @Bean
    public KafkaListenerEndpointRegistry getKafkaListenerEndpointRegistry(){
        return new KafkaListenerEndpointRegistry();
    }
}
