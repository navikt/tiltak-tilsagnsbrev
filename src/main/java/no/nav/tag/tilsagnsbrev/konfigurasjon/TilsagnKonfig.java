package no.nav.tag.tilsagnsbrev.konfigurasjon;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Configuration
public class TilsagnKonfig {

    @Autowired
    private MqKonfig mqKonfig;

    @Bean
    public ConnectionFactory connectionFactory() throws JMSException {
        MQConnectionFactory connectionFactory = new MQConnectionFactory();
        connectionFactory.setChannel(mqKonfig.getChannel());
        connectionFactory.setQueueManager(mqKonfig.getQueueManager());
        connectionFactory.setPort(Integer.valueOf(mqKonfig.getPort()));
        connectionFactory.setHostName(mqKonfig.getHost());
        connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        UserCredentialsConnectionFactoryAdapter adapter = new UserCredentialsConnectionFactoryAdapter();
        adapter.setTargetConnectionFactory(connectionFactory);
        adapter.setUsername(mqKonfig.getUser());
        adapter.setPassword(mqKonfig.getPassword());
        adapter.afterPropertiesSet();
        return adapter;
    }

    @Bean
    JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
         JmsTemplate jmsTemplate = new JmsTemplate();
         jmsTemplate.setConnectionFactory(connectionFactory);
         return jmsTemplate;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}