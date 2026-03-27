package no.nav.tag.tilsagnsbrev;

import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

@Profile({"preprod", "prod"})
@SpringBootApplication
@EnableOAuth2Client(cacheEnabled = true)
public class TilsagnsbrevApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder(TilsagnsbrevApplication.class)
				.initializers(new SjekkAktiveProfilerInitializer())
				.build();
		application.run(args);
	}

}
