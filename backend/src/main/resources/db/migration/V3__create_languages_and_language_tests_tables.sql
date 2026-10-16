CREATE TABLE languages (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(2)   NOT NULL,
    name VARCHAR(255) COLLATE "und-x-icu" NOT NULL,
    CONSTRAINT uk_languages_code UNIQUE (code)
);

CREATE TABLE language_tests (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    language_id BIGINT       NOT NULL,
    name        VARCHAR(255) COLLATE "und-x-icu" NOT NULL,
    description TEXT,
    CONSTRAINT fk_language_tests_language FOREIGN KEY (language_id) REFERENCES languages (id)
);

CREATE INDEX idx_language_tests_language_id ON language_tests (language_id);
