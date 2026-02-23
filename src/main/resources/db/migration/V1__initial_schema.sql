-- V1: Initial schema
-- Captures the base tables that existed before incremental migrations (V2+) were added.
-- Uses IF NOT EXISTS so re-running on an existing database is a safe no-op.

CREATE TABLE IF NOT EXISTS users (
    id       BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    age      INT          NOT NULL,
    gradyear INT          NOT NULL,
    college  VARCHAR(255) NOT NULL,
    major    VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_desired_job_titles (
    user_id   BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    job_title VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_skills (
    user_id BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    skill   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_experience (
    user_id    BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    experience VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS applications (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    job_title    VARCHAR(255),
    company      VARCHAR(255),
    status       VARCHAR(50),
    applied_date DATE,
    compensation INT,
    user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS resumes (
    id                BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id           BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    original_filename VARCHAR(255),
    stored_filename   VARCHAR(255),
    content_type      VARCHAR(100),
    size_bytes        BIGINT,
    uploaded_at       TIMESTAMP,
    storage_path      VARCHAR(500)
);
