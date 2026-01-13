ALTER TABLE word DROP COLUMN group_id;

ALTER TABLE word_enrichment ADD COLUMN group_id BIGINT REFERENCES word_group(id);
