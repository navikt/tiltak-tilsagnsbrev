package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.konfigurasjon.MqKonfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@Slf4j
@Component
@EnableJms
public class AltInn {


   @Autowired
   JmsTemplate jmsTemplate;

    @Autowired
    MqKonfig mqKonfig;


    public void sendTilsagnsbrev(String tilsagnAltinnXml){
        try{
            jmsTemplate.convertAndSend(mqKonfig.getQueue(), tilsagnAltinnXml);
        }catch(JmsException ex){
            log.error("Feil ved sending på MQ", ex);
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

}
