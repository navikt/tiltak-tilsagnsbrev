CREATE TABLE feilet_tilsagnsbrev (
  id uuid primary key,
  opprettet timestamp without time zone not null default now(),
  neste_steg varchar(10),
  retry integer,
  tilsagnsbrev(blob)
 );

