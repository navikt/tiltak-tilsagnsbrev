package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfService;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Tilsagnsbehandler {

    @Autowired
    TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    PdfService pdfService;

    public void behandleTilsagn(String goldenGateJson) {

        final Tilsagn tilsagn = tilsagnJsonMapper.goldengateJsonTilTilsagn(goldenGateJson);
        final String tilsagnJson = tilsagnJsonMapper.tilsagnTilJson(tilsagn);

        log.info("Tilsagnsbrev til pdfGen: {}", tilsagnJson);

        //final byte[] pdf = pdfService.tilsagnTilPdfBrev(tilsagnJson);



    }
}
