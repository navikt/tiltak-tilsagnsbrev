package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;

@Data
public class Correspondence {
    String ServiceCode;
    String ServiceEdition;
    String Reportee; //lik receier u. kommunenr
    Content Content;
    String AllowSystemDeleteDateTimeObject;
    String ArchiveReferenceObject;
    String ReplyOptionsObject;
    String NotificationsObject;
    String AllowForwardingObject;
    String CaseIDObject;
    String MessageSenderObject;
    String IsReservableObject;
}
