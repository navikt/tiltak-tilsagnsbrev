package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.ContainerStoppedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile({"preprod", "prod"})
@Slf4j
public class RestartPodComponent {
    @EventListener
    public void restartVedFeil(ContainerStoppedEvent event) {
        log.error("Restarter pod, fordi Spring har kastet ContainerStoppedEvent (melding {}). Dette kan skyldes autoriseringsproblemer.", event.toString());
        System.exit(1);
    }
}
