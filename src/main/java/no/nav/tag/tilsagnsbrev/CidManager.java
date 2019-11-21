package no.nav.tag.tilsagnsbrev;

import lombok.Data;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Data
@Component
public class CidManager {

    private static final String CID = "correlation-id";

    public UUID opprettCorrelationId() { //TODO Ev. hente en id fra Goldengate meldingen
        final UUID cid = UUID.randomUUID();
        MDC.put(CID,  cid.toString());
        return cid;
    }

    public void fjernCorrelationId(){
        Optional.ofNullable(MDC.get(CID)).ifPresent(cid -> MDC.remove(cid));
    }
}
