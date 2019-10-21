package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.integrasjon.ArenaTilsagnLytter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
//@Profile("preprod, prod")
@Slf4j
public class KafkaKonfig {

    @Bean
    public KafkaKonfig secret(){
        Map<String, String> env = System.getenv();


        String srvUser = env.get("KAFKA_USERNAME");
        String srvPwd = env.get("KAFKA_PASSWORD");

        if(StringUtils.isEmpty(srvUser)){
            log.error("srv-bruker mangler");
        }
        if(StringUtils.isEmpty(srvPwd)){
            log.error("srv-passord mangler");
        }


        System.setProperty("tiltak.tilsagnsbrev.serviceuser.bruker", srvUser);
        System.setProperty("tiltak.tilsagnsbrev.serviceuser.passord", srvPwd);
        return new KafkaKonfig();
    }

}
