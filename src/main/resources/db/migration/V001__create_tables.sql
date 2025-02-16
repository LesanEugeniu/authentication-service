CREATE TABLE users
(
    id             UUID PRIMARY KEY,
    email          VARCHAR(100) NOT NULL UNIQUE,
    password       VARCHAR(255) NOT NULL,
    first_name     VARCHAR(100),
    last_name      VARCHAR(100),
    email_verified BOOLEAN               DEFAULT false,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP
);

CREATE TABLE refresh_tokens
(
    id         UUID PRIMARY KEY,
    user_id    UUID                        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_user_id ON refresh_tokens (user_id);

CREATE SEQUENCE login_attempt_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE login_attempt
(
    id           BIGINT    NOT NULL DEFAULT NEXTVAL('login_attempt_id_sequence'),
    user_id      BIGINT    NOT NULL,
    attempt_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    success      BOOLEAN   NOT NULL,
    CONSTRAINT pk_login_attempt PRIMARY KEY (id)
);
