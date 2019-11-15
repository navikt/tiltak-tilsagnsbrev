package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Slf4j
@Service
public class Tilsagnsbehandler {

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    @Autowired
    private PdfGenService pdfService;

    @Autowired
    private AltInnService altInnService;

    public void behandleTilsagn(String goldenGateJson) {

        final Tilsagn tilsagn = tilsagnJsonMapper.goldengateMeldingTilTilsagn(goldenGateJson);
        log.info("Tilsagnsbrev til pdfGen: {}", tilsagn.getTilsagnNummer());

        final String tilsagnJson = tilsagnJsonMapper.tilsagnTilPdfJson(tilsagn);
        final byte[] base64Pdf = Base64.getEncoder().encode(pdfService.tilsagnTilPdfBrev(tilsagnJson));

        //Tilsagn til joark

        int kvittering = altInnService.sendTilsagnsbrev(tilsagnTilAltinnMapper.tilAltinnMelding(tilsagn, base64Pdf));
        log.info("Tilsagnsbrev med tilsagnsnr. til Altinn: {}", tilsagn.getTilsagnNummer());
    }
}
