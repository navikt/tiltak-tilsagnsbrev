package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.SingleConnectionFactory;

import javax.jms.ConnectionFactory;

@Configuration
@Profile("dev")
@Slf4j
public class JmsKonfigurasjon {

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new SingleConnectionFactory();
        connectionFactory.createContext("admin", "passw0rd");
        return connectionFactory;
    }
}





