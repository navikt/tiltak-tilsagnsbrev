CREATE TABLE feilet_tilsagnsbrev (
  id uuid primary key, //autogen
  opprettet timestamp without time zone not null default now(),
  er_datafeil boolean,
  neste_steg varchar(12),
  retry integer,
  tilsagn_data text
 );

