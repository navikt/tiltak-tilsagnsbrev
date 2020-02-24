package no.nav.tag.tilsagnsbrev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class LokalTilsagnsbrevApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder(LokalTilsagnsbrevApplication.class)
				.profiles("dev", "local")
				.build();
		application.run(args);
	}
}