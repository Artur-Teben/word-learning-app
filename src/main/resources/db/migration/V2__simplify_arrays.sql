ALTER TABLE word_enrichment DROP CONSTRAINT chk_usage_tags;

ALTER TABLE word_enrichment
    ALTER COLUMN usage_tags TYPE TEXT USING array_to_string(usage_tags, ','),
    ALTER COLUMN synonyms TYPE TEXT USING array_to_string(synonyms, ',');

ALTER TABLE word_enrichment
    ALTER COLUMN raw_ai_response TYPE TEXT;