package no.nav.tag.tilsagnsbrev.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.security.token.support.client.core.ClientProperties;
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import no.nav.team_tiltak.felles.persondata.PersondataClient;
import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Profile({"preprod", "prod"})
@EnableJwtTokenValidation
@EnableOAuth2Client(cacheEnabled = true)
public class PersondataServiceImpl implements PersondataService {
    private final PersondataClient persondataClient;

    public PersondataServiceImpl(
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

    @Override
    public Diskresjonskode hentDiskresjonskode(String fnr) {
        return persondataClient.hentDiskresjonskode(fnr).orElse(Diskresjonskode.UGRADERT);
    }

}
