package no.nav.tag.tilsagnsbrev.behandler;

import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.TilsagnException;
import no.nav.tag.tilsagnsbrev.feilet.TilsagnBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJournalpostMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OppgaverTest {

    @Mock
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Mock
    private TilsagnBehandler tilsagnBehandler;

    @Mock
    private PdfGenService pdfService;

    @Mock
    private JoarkService joarkService;

    @Mock
    private AltInnService altInnService;

    @Mock
    private TilsagnJournalpostMapper tilsagnJournalpostMapper;

    @Mock
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    @InjectMocks
    private Oppgaver oppgaver;

    private static final String PDF_JSON = "pdfJson";
    private static final byte[] PDF_BYTEARRAY = "pdf".getBytes();
    private static final String TILSAGN_DATA = Testdata.hentFilString("TILSAGN_DATA.json");
    private static final Journalpost JOURNALPOST = mock(Journalpost.class);
    private static final InsertCorrespondenceBasicV2 ALTINN_WS_REQUEST = mock(InsertCorrespondenceBasicV2.class);

    @BeforeEach
    public void setUp() {
//


    }
    
    @Test
    public void behandlerTilsagnEtterEnManuellBehandlingAvArenaMappingFeil() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().build();
        when(tilsagnTilAltinnMapper.tilAltinnMelding(null, null)).thenReturn(ALTINN_WS_REQUEST);

        oppgaver.utfoerOppgaver(tub);

        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(eq(tub));
        verify(tilsagnJsonMapper, times(1)).opprettPdfJson(any(TilsagnUnderBehandling.class));
        verify(pdfService, times(1)).tilsagnsbrevTilPdfBytes(any(TilsagnUnderBehandling.class), any());
        verify(tilsagnJournalpostMapper, times(1)).tilsagnTilJournalpost(any(TilsagnUnderBehandling.class));
        verify(joarkService, times(1)).sendJournalpost(any());
        verifySendTilAltinn();
        verify(tilsagnBehandler, times(1)).lagreStatus(any(TilsagnUnderBehandling.class));
    }
    
    private void verifyJournalforing() {
        verify(tilsagnJournalpostMapper, times(1)).tilsagnTilJournalpost(any(TilsagnUnderBehandling.class));
        verify(joarkService, times(1)).sendJournalpost(JOURNALPOST);
    }
    
    private void verifySendTilAltinn() {
        verify(altInnService, times(1)).sendTilsagnsbrev(ALTINN_WS_REQUEST);
    }

    @Test
    public void behandlerTilsagnEtterFeiletJournalforing() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().json(TILSAGN_DATA).pdf(PDF_BYTEARRAY).tilsagnsbrevId(1).mappetFraArena(true).altinnReferanse(002).build();
        when(tilsagnJournalpostMapper.tilsagnTilJournalpost(any(TilsagnUnderBehandling.class))).thenReturn(JOURNALPOST);

        oppgaver.utfoerOppgaver(tub);
        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(any(TilsagnUnderBehandling.class));

        verifyJournalforing();
        verifyNoInteractions(pdfService, altInnService);
        verify(tilsagnBehandler, times(1)).lagreStatus(any(TilsagnUnderBehandling.class));
    }


    @Test
    public void behandlerTilsagnEtterFeiletAltinnSending() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().json(TILSAGN_DATA).pdf(PDF_BYTEARRAY).tilsagnsbrevId(1).mappetFraArena(true).journalpostId("1").build();
        when(tilsagnTilAltinnMapper.tilAltinnMelding(null, PDF_BYTEARRAY)).thenReturn(ALTINN_WS_REQUEST);

        oppgaver.utfoerOppgaver(tub);
        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(any(TilsagnUnderBehandling.class));
        verifySendTilAltinn();
        verify(tilsagnBehandler, times(1)).lagreStatus(any(TilsagnUnderBehandling.class));
    }
    
    @Test
    public void avbryterHvisOpprettTilsagnFeiler() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().build();

        doThrow(DataException.class).when(tilsagnJsonMapper).opprettTilsagn(tub);
        
        oppgaver.utfoerOppgaver(tub);

        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(any(TilsagnUnderBehandling.class));
        verifyNoInteractions(pdfService, altInnService, tilsagnJournalpostMapper, joarkService);
        verify(tilsagnBehandler, times(1)).lagreEllerOppdaterFeil(eq(tub), any(TilsagnException.class));
    }

    @Test
    public void avbryterHvisOpprettPdfDokFeiler() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().build();

        when(tilsagnJsonMapper.opprettPdfJson(tub)).thenThrow(DataException.class);

        oppgaver.utfoerOppgaver(tub);

        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(tub);
        verify(tilsagnJsonMapper, times(1)).opprettPdfJson(tub);
        verifyNoInteractions(pdfService, altInnService, tilsagnJournalpostMapper, joarkService);
        verify(tilsagnBehandler, times(1)).lagreEllerOppdaterFeil(eq(tub), any(TilsagnException.class));
    }

}
