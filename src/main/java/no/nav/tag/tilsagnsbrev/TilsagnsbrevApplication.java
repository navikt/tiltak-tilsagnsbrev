package no.nav.tag.tilsagnsbrev;

import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableJwtTokenValidation
@EnableOAuth2Client(cacheEnabled = true)
@Profile({"preprod", "prod"})
public class TilsagnsbrevApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder(TilsagnsbrevApplication.class)
				.initializers(new SjekkAktiveProfilerInitializer())
				.build();
		application.run(args);
	}

}
