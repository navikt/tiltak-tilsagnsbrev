package no.nav.tag.tilsagnsbrev.integrasjon.sts.ws;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.addressing.WSAddressingFeature;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class WsClient {

    @SafeVarargs
    public static <T> T createPort(String serviceUrl, Class<T> portType, PhaseInterceptor<? extends Message>... interceptors) {
        return createPort(serviceUrl, portType, null, null, interceptors);
    }

    @SafeVarargs
    public static <T> T createPort(String serviceUrl, Class<T> portType, URL wsdlUrl, QName serviceName, PhaseInterceptor<? extends Message>... interceptors) {
        return createPort(serviceUrl, portType, wsdlUrl, serviceName, null, interceptors);
    }

    @SafeVarargs
    public static <T> T createPort(String serviceUrl, Class<T> portType, URL wsdlUrl, QName serviceName, QName endpointName, PhaseInterceptor<? extends Message>... interceptors) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(portType);
        factory.setAddress(Objects.requireNonNull(serviceUrl));
        if (wsdlUrl != null) {
            factory.setWsdlURL(wsdlUrl.toString());
        }
        if (serviceName != null) {
            factory.setServiceName(serviceName);
        }
        if (endpointName != null) {
            factory.setEndpointName(endpointName);
        }
        factory.getFeatures().add(new WSAddressingFeature());
        factory.getFeatures().add(new LoggingFeature());
        T port = portType.cast(factory.create());
        Client client = ClientProxy.getClient(port);
        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        conduit.getClient().setVersion("1.1");
        Arrays.stream(interceptors).forEach(client.getOutInterceptors()::add);
        return port;
    }

}
