package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnXmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Tilsagnsbehandler {

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private TilsagnXmlMapper tilsagnXmlMapper;

    @Autowired
    private PdfGenService pdfService;

    @Autowired
    private AltInnService altInnService;

    public void behandleTilsagn(String goldenGateJson) {

        final Tilsagn tilsagn = tilsagnJsonMapper.goldengateMeldingTilTilsagn(goldenGateJson);
        log.info("Tilsagnsbrev til pdfGen: {}", tilsagn.getTilsagnNummer());

        final String tilsagnJson = tilsagnJsonMapper.tilsagnTilPdfJson(tilsagn);
        final byte[] pdf = pdfService.tilsagnTilPdfBrev(tilsagnJson);

        final String tilsagnXml = tilsagnXmlMapper.tilAltinnMelding(tilsagn, pdf);
        log.info("Tilsagnsbrev med tilsagnsnr. til Altinn: {}", tilsagn.getTilsagnNummer());

        altInnService.sendTilsagnsbrev(tilsagnXml);

        //Tilsagn til joark

    }
}
