package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJournalpostMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import no.nav.tag.tilsagnsbrev.mapper.json.TilsagnJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Oppgaver {

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private AltInnService altInnService;

    @Autowired
    private TilsagnJournalpostMapper tilsagnJournalpostMapper;

    @Autowired
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    public void arenaMeldingTilTilsagnData(TilsagnUnderBehandling tilsagnUnderBehandling){
        tilsagnJsonMapper.arenaMeldingTilTilsagn(tilsagnUnderBehandling);
        tilsagnJsonMapper.tilsagnTilJson(tilsagnUnderBehandling);
    }


    public void journalfoerTilsagnsbrev(TilsagnUnderBehandling tilsagnUnderBehandling, byte[] pdf) {
        try {
            Journalpost journalpost = tilsagnJournalpostMapper.tilsagnTilJournalpost(tilsagnUnderBehandling.getTilsagn(), pdf);
            tilsagnUnderBehandling.setJournalpostId(joarkService.sendJournalpost(journalpost));
            log.info("Journalført tilsagnsbrev, journalpostId: {}", journalpost);
        }catch (DataException de){
            throw de;
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    public void sendTilAltinn(TilsagnUnderBehandling tilsagnUnderBehandling, byte[] pdf) {
        log.info("Sender tilsagnsbrev til Altinn");
        InsertCorrespondenceBasicV2 wsRequest = mapTilWebserviceRequest(tilsagnUnderBehandling, pdf);
        sentWsRequest(tilsagnUnderBehandling, wsRequest);
        log.info("Tilsagnsbrev er sendt til Altinn. kvittering {}", tilsagnUnderBehandling.getAltinnKittering());
    }


    private InsertCorrespondenceBasicV2 mapTilWebserviceRequest(TilsagnUnderBehandling tilsagnUnderBehandling, byte[] pdf) {
        try {
            return tilsagnTilAltinnMapper.tilAltinnMelding(tilsagnUnderBehandling.getTilsagn(), pdf);
        } catch (Exception e) {
            throw new DataException(e.getMessage());
        }
    }

    private void sentWsRequest(TilsagnUnderBehandling tilsagnUnderBehandling, InsertCorrespondenceBasicV2 wsRequest) {
        try{
            int kvitteringId = altInnService.sendTilsagnsbrev(wsRequest);
            tilsagnUnderBehandling.setAltinnKittering(kvitteringId);
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }
}
