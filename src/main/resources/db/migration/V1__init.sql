CREATE TABLE tilsagn_under_behandling (
  cid uuid primary key,
  opprettet timestamp without time zone not null default now(),
  tilsagnsbrev_id integer,
  retry integer,
  datafeil boolean,
  mappet_fra_arena boolean,
  journalpost_id varchar(9),
  altinn_referanse varchar(30),
  behandlet boolean,
  json text,
  pdf bytea
 );

 CREATE TABLE tilsagn_logg (
  id uuid primary key,
  tilsagnsbrev_id integer,
  tidspunkt_lest timestamp without time zone not null default now()
);

