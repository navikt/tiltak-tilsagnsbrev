ALTER TABLE tilsagn_under_behandling ADD COLUMN TILSAGNSBREV_ID integer;

CREATE TABLE tilsagn_logg (
  tilsagnsbrev_id integer primary key,
  opprettet timestamp without time zone not null default now()
);



