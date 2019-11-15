CREATE TABLE tilsagn_under_behandling (
  cid uuid primary key,
  feilmelding varchar(255),
  opprettet timestamp without time zone not null default now(),
  er_datafeil boolean,
  neste_steg varchar(12),
  retry integer,
  json text
 );

