ALTER TABLE word_enrichment
    ADD COLUMN category_id BIGINT REFERENCES category(id);