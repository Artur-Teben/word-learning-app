-- Enable case-insensitive text type for clean deduplication of word.text
CREATE EXTENSION IF NOT EXISTS citext;

-- =========================
-- 1) CATEGORY (fixed list)
-- =========================
CREATE TABLE category
(
    id   BIGSERIAL PRIMARY KEY,
    code VARCHAR(64)  NOT NULL UNIQUE, -- e.g. FOOD, TRAVEL
    name VARCHAR(128) NOT NULL         -- e.g. "Food & Cooking"
);

-- =========================
-- 2) WORD_GROUP (dynamic clusters)
-- =========================
CREATE TABLE word_group
(
    id          BIGSERIAL PRIMARY KEY,
    category_id BIGINT       NOT NULL REFERENCES category (id),
    group_key   VARCHAR(96)  NOT NULL, -- stable key e.g. COOKING_METHODS
    name        VARCHAR(128) NOT NULL, -- label e.g. "Cooking methods"
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT uq_group_category_key UNIQUE (category_id, group_key)
);

CREATE INDEX idx_word_group_category_id ON word_group (category_id);

-- =========================
-- 3) WORD (raw capture + statuses)
-- =========================
CREATE TABLE word
(
    id                BIGSERIAL PRIMARY KEY,
    text              CITEXT      NOT NULL, -- user input, case-insensitive
    context           TEXT,

    processing_status VARCHAR(16) NOT NULL DEFAULT 'NEW',
    learning_status   VARCHAR(16) NOT NULL DEFAULT 'TO_REVIEW',

    group_id          BIGINT REFERENCES word_group (id),

    created_at        TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP   NOT NULL DEFAULT now(),

    CONSTRAINT uq_word_text UNIQUE (text),

    CONSTRAINT chk_processing_status CHECK (
        processing_status IN ('NEW', 'ENRICHING', 'READY', 'FAILED')
        ),
    CONSTRAINT chk_learning_status CHECK (
        learning_status IN ('TO_REVIEW', 'LEARNING', 'LEARNED', 'DISCARDED')
        )
);

CREATE INDEX idx_word_processing_status ON word (processing_status);
CREATE INDEX idx_word_learning_status ON word (learning_status);
CREATE INDEX idx_word_group_id ON word (group_id);

-- =========================
-- 4) WORD_ENRICHMENT (AI output)
-- =========================
CREATE TABLE word_enrichment
(
    word_id         BIGINT PRIMARY KEY REFERENCES word (id) ON DELETE CASCADE,

    lemma           VARCHAR(255) NOT NULL,
    part_of_speech  VARCHAR(32),

    meaning_en      TEXT         NOT NULL,
    translation_uk  TEXT         NOT NULL,
    transcription   VARCHAR(128),

    commonness      VARCHAR(24)  NOT NULL,
    cefr_level      VARCHAR(2),

    usage_tags      TEXT[]       NOT NULL DEFAULT '{}',
    synonyms        TEXT[]       NOT NULL DEFAULT '{}',

    model           VARCHAR(128),
    raw_ai_response JSONB,
    enriched_at     TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT chk_commonness CHECK (
        commonness IN ('VERY_COMMON', 'COMMON', 'MODERATE', 'UNCOMMON', 'RARE')
        ),
    CONSTRAINT chk_cefr_level CHECK (
        cefr_level IS NULL OR cefr_level IN ('A1', 'A2', 'B1', 'B2', 'C1', 'C2')
        ),
    CONSTRAINT chk_usage_tags CHECK (
        usage_tags <@ ARRAY [
            'ARCHAIC',
            'SLANG',
            'TECHNICAL',
            'FORMAL',
            'INFORMAL',
            'LITERARY',
            'VULGAR',
            'DIALECT',
            'JARGON'
            ]::text[]
        )
);

CREATE INDEX idx_enrichment_commonness ON word_enrichment (commonness);
CREATE INDEX idx_enrichment_cefr_level ON word_enrichment (cefr_level);

-- =========================
-- updated_at trigger for word
-- =========================
CREATE OR REPLACE FUNCTION set_updated_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_word_set_updated_at
    BEFORE UPDATE
    ON word
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

-- =========================
-- Seed categories (broad coverage)
-- =========================
INSERT INTO category (code, name)
VALUES ('GENERAL', 'General / Other'),
       ('PEOPLE', 'People & Relationships'),
       ('EMOTIONS', 'Emotions & Feelings'),
       ('COMMUNICATION', 'Communication & Language'),
       ('WORK', 'Work & Business'),
       ('EDUCATION', 'Education & Learning'),
       ('HOME', 'Home & Household'),
       ('FOOD', 'Food & Cooking'),
       ('HEALTH', 'Health & Body'),
       ('SPORT', 'Sports & Fitness'),
       ('TRAVEL', 'Travel & Transport'),
       ('NATURE', 'Nature & Weather'),
       ('TIME', 'Time & Frequency'),
       ('MONEY', 'Money & Finance'),
       ('LAW', 'Law & Rules'),
       ('TECH', 'Technology'),
       ('ART', 'Art & Culture'),
       ('MEDIA', 'Media & Entertainment'),
       ('CLOTHING', 'Clothing & Appearance'),
       ('CITY', 'Places & City Life'),
       ('ACTIONS', 'Actions & Movement'),
       ('DESCRIPTIONS', 'Descriptions & Qualities'),
       ('OBJECTS', 'Objects & Materials'),
       ('SOCIAL', 'Society & Politics'),
       ('MENTAL', 'Mind & Thinking');