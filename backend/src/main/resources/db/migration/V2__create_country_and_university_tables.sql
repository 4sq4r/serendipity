CREATE TABLE country (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(2)   NOT NULL,
    name VARCHAR(255) COLLATE "und-x-icu" NOT NULL,
    CONSTRAINT uk_country_code UNIQUE (code)
);

CREATE TABLE university (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    country_id  BIGINT        NOT NULL,
    name        VARCHAR(255)  COLLATE "und-x-icu" NOT NULL,
    website     VARCHAR(2048),
    description TEXT,
    CONSTRAINT fk_university_country FOREIGN KEY (country_id) REFERENCES country (id)
);

CREATE INDEX idx_university_country_id ON university (country_id);
