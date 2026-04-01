ALTER TABLE tilsagn_under_behandling RENAME COLUMN pdf TO pdf_altinn;
ALTER TABLE tilsagn_under_behandling ADD COLUMN pdf_joark bytea;
