CREATE TABLE countries (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(2)   NOT NULL,
    name VARCHAR(255) COLLATE "und-x-icu" NOT NULL,
    CONSTRAINT uk_countries_code UNIQUE (code)
);

CREATE TABLE universities (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    country_id  BIGINT        NOT NULL,
    name        VARCHAR(255)  COLLATE "und-x-icu" NOT NULL,
    website     VARCHAR(2048),
    description TEXT,
    CONSTRAINT fk_universities_country FOREIGN KEY (country_id) REFERENCES countries (id)
);

CREATE INDEX idx_universities_country_id ON universities (country_id);
