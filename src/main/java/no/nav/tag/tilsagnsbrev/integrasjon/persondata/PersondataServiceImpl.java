package no.nav.tag.tilsagnsbrev.integrasjon.persondata;

import lombok.extern.slf4j.Slf4j;
import no.nav.security.token.support.client.core.ClientProperties;
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.tag.tilsagnsbrev.konfigurasjon.PersondataKonfig;
import no.nav.team_tiltak.felles.persondata.PersondataClient;
import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Profile({"preprod", "prod"})
public class PersondataServiceImpl implements PersondataService {
    private final PersondataClient persondataClient;

    public PersondataServiceImpl(
        PersondataKonfig persondataProperties,
        OAuth2AccessTokenService oAuth2AccessTokenService,
        ClientConfigurationProperties clientConfigurationProperties
    ) {
        ClientProperties clientProperties = clientConfigurationProperties.getRegistration().get("pdl-api");

        this.persondataClient = new PersondataClient(
            persondataProperties.getUri(),
            () -> Optional.ofNullable(clientProperties)
                .map(prop -> oAuth2AccessTokenService.getAccessToken(prop).getAccessToken())
                .orElse(null)
        );
    }

    @Override
    public Diskresjonskode hentDiskresjonskode(String fnr) {
        return persondataClient.hentDiskresjonskode(fnr).orElse(Diskresjonskode.UGRADERT);
    }

}
