package no.nav.tag.tilsagnsbrev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TilsagnsbrevApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder(TilsagnsbrevApplication.class)
				.initializers(new SjekkAktiveProfilerInitializer())
				.build();
		application.run(args);
	}

}