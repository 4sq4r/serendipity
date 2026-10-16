CREATE TABLE language (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(2)   NOT NULL,
    name VARCHAR(255) COLLATE "und-x-icu" NOT NULL,
    CONSTRAINT uk_language_code UNIQUE (code)
);

CREATE TABLE language_test (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    language_id BIGINT       NOT NULL,
    name        VARCHAR(255) COLLATE "und-x-icu" NOT NULL,
    description TEXT,
    CONSTRAINT fk_language_test_language FOREIGN KEY (language_id) REFERENCES language (id)
);

CREATE INDEX idx_language_test_language_id ON language_test (language_id);
