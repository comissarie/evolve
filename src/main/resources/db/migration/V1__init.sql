CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE ip_address (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ip TEXT UNIQUE NOT NULL
);

CREATE TABLE app_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nickname TEXT UNIQUE NOT NULL,
    email TEXT NULL,
    vk_id TEXT NULL
);

CREATE TABLE user_ip (
    user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    ip_id UUID NOT NULL REFERENCES ip_address(id) ON DELETE CASCADE,
    UNIQUE (user_id, ip_id)
);

CREATE TABLE squad (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE squad_user (
    squad_id UUID NOT NULL REFERENCES squad(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    UNIQUE (squad_id, user_id)
);

CREATE TABLE login_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nickname TEXT NOT NULL,
    ip TEXT NOT NULL,
    ts TIMESTAMP NOT NULL,
    success BOOLEAN NOT NULL
);

CREATE TABLE message_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nickname TEXT NOT NULL,
    ip TEXT NOT NULL,
    message TEXT NOT NULL,
    ts TIMESTAMP NOT NULL
);

CREATE INDEX idx_login_history_nickname ON login_history(nickname);
CREATE INDEX idx_message_history_nickname ON message_history(nickname);
