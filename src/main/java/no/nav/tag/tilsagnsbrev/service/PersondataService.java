package no.nav.tag.tilsagnsbrev.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.security.token.support.client.core.ClientProperties;
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.team_tiltak.felles.persondata.PersondataClient;
import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PersondataService {
    private final PersondataClient persondataClient;

    public PersondataService(
        PersondataProperties persondataProperties,
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

    public Diskresjonskode hentDiskresjonskode(String fnr) {
        return persondataClient.hentDiskresjonskode(fnr).orElse(Diskresjonskode.UGRADERT);
    }

}
