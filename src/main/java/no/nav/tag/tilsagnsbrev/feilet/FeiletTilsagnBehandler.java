package no.nav.tag.tilsagnsbrev.feilet;

import no.nav.tag.tilsagnsbrev.controller.exception.DataException;
import org.springframework.stereotype.Component;

@Component
public class FeiletTilsagnBehandler {

    public void lagreFeil(RuntimeException e) {
        if(e instanceof DataException){
            lagreSomDatafeil((DataException) e);
            return;
        }
        lagreSomSystemfeil(e);
    }

    private void lagreSomSystemfeil(RuntimeException e) {
    }

    private void lagreSomDatafeil(DataException e){
        
    }

}
