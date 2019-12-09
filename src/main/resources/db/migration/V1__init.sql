CREATE TABLE tilsagn_under_behandling (
  cid uuid primary key,
  opprettet timestamp without time zone not null default now(),
  mappet_fra_arena boolean,
  retry integer,
  datafeil boolean,
  journalpost_id varchar(9),
  altinn_kittering integer,
  feilmelding varchar(255),
  behandlet boolean,
  json text
 );

