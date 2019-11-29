ALTER TABLE tilsagn_under_behandling ADD COLUMN TILSAGNSBREV_ID integer;

CREATE TABLE tilsagn_logg (
  id uuid primary key,
  tilsagnsbrev_id integer,
  tidspunkt_lest timestamp without time zone not null default now()
);



