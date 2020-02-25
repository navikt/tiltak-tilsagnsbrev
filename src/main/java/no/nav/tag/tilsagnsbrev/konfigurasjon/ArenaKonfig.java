package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

@Slf4j
@Configuration
public class ArenaKonfig {

    @Bean
    public KafkaListenerErrorHandler customKafkaErrLogger(){
        return (Message<?> msg, ListenerExecutionFailedException exc) -> {
            log.error("Feil ved henting av tilsagn fra topic: " + msg.getHeaders(), exc);
            return null;
        };
    }
}
