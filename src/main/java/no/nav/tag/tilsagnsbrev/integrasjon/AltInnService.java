package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;


@Slf4j
@Service
public class AltInnService {

    private static final String SOAPAction = "http://www.altinn.no/services/ServiceEngine/Correspondence/2009/10/ICorrespondenceAgencyExternalBasic/InsertCorrespondenceBasicV2";


    @Autowired
    private WebServiceTemplate webServiceTemplate;

    public void sendTilsagnsbrev(InsertCorrespondenceBasicV2 insertCorrespondenceBasicV2) {

        SoapActionCallback callback = new SoapActionCallback(SOAPAction);
        this.webServiceTemplate.marshalSendAndReceive(insertCorrespondenceBasicV2, callback);

    }
}
