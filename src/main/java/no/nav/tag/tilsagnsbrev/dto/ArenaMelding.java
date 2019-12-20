package no.nav.tag.tilsagnsbrev.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArenaMelding {
    private String table;
    private String op_type;
    private String op_ts;
    private String current_ts;
    private String pos;
    private ObjectNode after;
}
