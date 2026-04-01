package no.nav.tag.tilsagnsbrev.service;

import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class PersondataServiceFake implements PersondataService {

    @Override
    public Diskresjonskode hentDiskresjonskode(String fnr) {
        return Diskresjonskode.UGRADERT;
    }

}
