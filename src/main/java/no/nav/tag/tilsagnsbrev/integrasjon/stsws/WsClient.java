package no.nav.tag.tilsagnsbrev.integrasjon.stsws;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.cxf.ws.addressing.WSAddressingFeature;

import java.util.Arrays;
import java.util.Objects;

public class WsClient {

    @SuppressWarnings("unchecked")
    public static <T> T createPort(String serviceUrl, Class<T> portType, PhaseInterceptor<? extends Message>... interceptors) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(portType);
        factory.setAddress(Objects.requireNonNull(serviceUrl));
        factory.getFeatures().add(new WSAddressingFeature());
        factory.getFeatures().add(new LoggingFeature());
        T port = (T) factory.create();
        Client client = ClientProxy.getClient(port);
        Arrays.stream(interceptors).forEach(client.getOutInterceptors()::add);
        return port;
    }

}
