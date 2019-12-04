package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.AllArgsConstructor;
import no.altinn.services.serviceengine.correspondence._2009._10.ICorrespondenceAgencyExternalBasic;
import no.altinn.services.serviceengine.correspondence._2009._10.ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage;
import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AltInnService {

    private final ICorrespondenceAgencyExternalBasic iCorrespondenceAgencyExternalBasic;

    public int sendTilsagnsbrev(InsertCorrespondenceBasicV2 insertCorrespondenceBasicV2) {
        try {
            return iCorrespondenceAgencyExternalBasic.insertCorrespondenceBasicV2(
                    insertCorrespondenceBasicV2.getSystemUserName(),
                    insertCorrespondenceBasicV2.getSystemPassword(),
                    insertCorrespondenceBasicV2.getSystemUserCode(),
                    insertCorrespondenceBasicV2.getExternalShipmentReference(),
                    insertCorrespondenceBasicV2.getCorrespondence()
            ).getReceiptId();
        } catch (ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage fault) {
            throw new RuntimeException(fault);
        }
    }
}
