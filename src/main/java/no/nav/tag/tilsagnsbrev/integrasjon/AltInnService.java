package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.altinn.InsertCorrespondenceBasicV2;
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

    public void sendTilsagnsbrev(InsertCorrespondenceBasicV2 soapBody) {

        SoapActionCallback callback = new SoapActionCallback(SOAPAction);
        this.webServiceTemplate.marshalSendAndReceive(soapBody, callback);

    }
}
