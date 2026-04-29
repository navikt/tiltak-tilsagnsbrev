package no.nav.tag.tilsagnsbrev.integrasjon.persondata;

import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;

public interface PersondataService {
    Diskresjonskode hentDiskresjonskode(String fnr);
}
