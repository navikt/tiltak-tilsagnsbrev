# tiltak-tilsagnsbrev
Tilsagnsbrev opprettet i Arena får en pdf-fil opprettet som journalføres og sendes til mottaker via Altinn.

Kjør applikasjonen lokalt med spring-boot profilene 'dev' og 'local'.
Legg til profilen 'testdata' for å populere h2 databsen.

H2 konsoll url: http://localhost:8082/tiltak-tilsagnsbrev/h2-console

JDBC URL: jdbc:h2:mem:testdb
Bruker: sa, Passord: blank
.

⚠️ Before going to production (manual steps)

1. Create the Altinn 3 resource in Altinn Studio — suggested ID: nav-tilsagnsbrev
2. Request scopes altinn:serviceowner + altinn:correspondence.write from servicedesk@altinn.no
3. Register Maskinporten client with those scopes in Samarbeidsportalen
4. Set env vars in NAIS: altinn.base.url and altinn.resource.id for production
