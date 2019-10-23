
package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TilsagnNummer {

    private String aar;
    private String loepenrSak;
    private String loepenrTilsagn;

}
