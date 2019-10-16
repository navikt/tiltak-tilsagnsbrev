package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.stereotype.Service;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@Service
@EnableJms
public class AltUt {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;
    
    ConnectionFactory connectionFactory;

    MarshallingMessageConverter marshallingMessageConverter;

    public void sendTilsagnsbrev(Tilsagn tilsagn){

        SingleConnectionFactory connectionFactory = new SingleConnectionFactory();
        connectionFactory.createContext("admin", "passw0rd");
        connectionFactory.afterPropertiesSet();


        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.convertAndSend("QM1", "halloi");



    }

}
